package com.example.services.AuthService;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;
import com.example.model.entities.db.UserInfo;
import com.example.model.entities.redis.AccessToken;
import com.example.model.entities.redis.BaseToken;
import com.example.model.entities.redis.RefreshToken;
import com.example.model.mappers.UserMapper;
import com.example.repositories.db.UserRepository;
import com.example.repositories.redis.AccessTokenRepository;
import com.example.repositories.redis.RefreshTokenRepository;
import com.example.security.utils.JwtUtils.JwtUtils;
import lombok.AllArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@AllArgsConstructor
public class ThesisAuthService implements AuthService {
    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private final Supplier<ResponseStatusException> unauthorizedExceptionHandler = () -> new ResponseStatusException(UNAUTHORIZED, "Unauthorized");

    @Override
    public AuthResponse loginUser(AuthRequest authRequest) {
        var user = checkUser(authRequest);

        return createTokens(user.getUsername());
    }

    @Override
    public AuthResponse registerUser(AuthRequest authRequest) {
        var user = createUser(authRequest);

        return createTokens(user.getUsername());
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        final var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User wasn't found"));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void logoutUser(String username) {
        disableAccessTokensByUsername(username);
        disableRefreshTokensByUsername(username);
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest refreshRequest) {
        var refreshToken = refreshTokenRepository.findById(refreshRequest.getRefresh())
                .orElseThrow(unauthorizedExceptionHandler);

        if (refreshToken.getIsActive()) {
            disableToken(refreshToken);
            refreshTokenRepository.save(refreshToken);

            return createTokens(refreshToken.getUsername());
        }

        throw unauthorizedExceptionHandler.get();
    }

    private AuthResponse createTokens(String username) {
        var access = createAccessToken(username);
        var refresh = createRefreshToken(username);

        return new AuthResponse(access.getToken(), refresh.getToken());
    }

    private RefreshToken createRefreshToken(String username) {
        var tokenHash = jwtUtils.createJWTRefreshToken(username);
        var token = new RefreshToken(tokenHash, username, true);
        refreshTokenRepository.save(token);

        return token;
    }

    private AccessToken createAccessToken(String username) {
        var tokenHash = jwtUtils.createJWTAccessToken(username);
        var token = new AccessToken(tokenHash, username, true);
        accessTokenRepository.save(token);

        return token;
    }

    private UserInfo checkUser(AuthRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(unauthorizedExceptionHandler);
        var encryptedPassword = passwordEncoder.encode(authRequest.getPassword());

        if (user.getPassword().equals(encryptedPassword)) {
            return user;
        }

        throw unauthorizedExceptionHandler.get();
    }

    private UserInfo createUser(AuthRequest authRequest) {
        var newUserInfo = UserMapper.INSTANCE.toUserInfo(authRequest);
        newUserInfo.setPassword(passwordEncoder.encode(newUserInfo.getPassword()));

        userRepository.save(newUserInfo);

        return newUserInfo;
    }

    private void disableAccessTokensByUsername(String username) {
        var accessTokens = accessTokenRepository.findByUsername(username);
        accessTokens.forEach(this::disableToken);
        accessTokenRepository.saveAll(accessTokens);
    }

    private void disableRefreshTokensByUsername(String username) {
        var refreshTokens = refreshTokenRepository.findByUsername(username);
        refreshTokens.forEach(this::disableToken);
        refreshTokenRepository.saveAll(refreshTokens);
    }

    private void disableToken(BaseToken token) {
        token.setIsActive(false);
    }
}
