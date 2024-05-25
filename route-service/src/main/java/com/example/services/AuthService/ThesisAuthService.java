package com.example.services.AuthService;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;
import com.example.model.entities.db.UserInfo;
import com.example.model.entities.redis.AccessToken;
import com.example.model.entities.redis.RefreshToken;
import com.example.model.mappers.UserMapper;
import com.example.repositories.db.UserRepository;
import com.example.repositories.redis.AccessTokenRepository;
import com.example.repositories.redis.RefreshTokenRepository;
import com.example.security.utils.JwtUtils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@AllArgsConstructor
@Slf4j
public class ThesisAuthService implements AuthService {
    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private final Supplier<ResponseStatusException> unauthorizedExceptionHandler = () -> new ResponseStatusException(UNAUTHORIZED, "Unauthorized");

    @Override
    public AuthResponse loginUser(AuthRequest authRequest, String fingerPrint) {
        var user = checkUser(authRequest);

        return createTokens(user.getUsername(), fingerPrint);
    }

    @Override
    public AuthResponse registerUser(AuthRequest authRequest, String fingerPrint) {
        var user = createUser(authRequest);

        return createTokens(user.getUsername(), fingerPrint);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        final var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User wasn't found"));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void logoutUser(String username, String fingerPrint) {
        disableAccessTokensByUsername(username);
        disableRefreshTokensByFingerPrint(fingerPrint);
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest refreshRequest, String fingerPrint) {
        var refreshToken = refreshTokenRepository.findById(refreshRequest.getRefresh())
                .orElseThrow(unauthorizedExceptionHandler);

        disableAccessTokensByUsername(refreshToken.getUsername());
        disableRefreshTokensByFingerPrint(fingerPrint);

        if (refreshToken.getIsActive()) {
            refreshTokenRepository.save(refreshToken);
            return createTokens(refreshToken.getUsername(), fingerPrint);
        } else if (!fingerPrint.equals(refreshToken.getFingerPrint())) {
            disableRefreshTokensByUsername(refreshToken.getUsername());
        }

        throw unauthorizedExceptionHandler.get();
    }

    private AuthResponse createTokens(String username, String fingerPrint) {
        var access = createAccessToken(username);
        var refresh = createRefreshToken(username, fingerPrint);

        return new AuthResponse(access.getToken(), refresh.getToken());
    }

    private RefreshToken createRefreshToken(String username, String fingerPrint) {
        var tokenHash = jwtUtils.createJWTRefreshToken(username);
        var token = new RefreshToken(tokenHash, username, true, fingerPrint);
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

        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
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
        accessTokens.forEach(token -> token.setIsActive(false));
        accessTokenRepository.saveAll(accessTokens);
    }

    private void disableRefreshTokensByUsername(String username) {
        var refreshTokens = refreshTokenRepository.findByUsername(username);
        refreshTokens.forEach(token -> token.setIsActive(false));
        refreshTokenRepository.saveAll(refreshTokens);
    }

    private void disableRefreshTokensByFingerPrint(String fingerPrint) {
        var refreshTokens = refreshTokenRepository.findByFingerPrint(fingerPrint);
        refreshTokens.forEach(token -> token.setIsActive(false));
        refreshTokenRepository.saveAll(refreshTokens);
    }
}
