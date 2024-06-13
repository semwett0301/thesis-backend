package com.example.services;

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
import com.example.services.AuthService.ThesisAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
public class ThesisAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ThesisAuthService thesisAuthService;

    private AuthRequest authRequest;
    private UserInfo userInfo;
    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("username", "password");
        userInfo = new UserInfo();
        userInfo.setUsername("username");
        userInfo.setPassword("encodedPassword");
        accessToken = new AccessToken("accessToken", "username", true);
        refreshToken = new RefreshToken("refreshToken", "username", true, "fingerPrint");
    }

    @Test
    void loginUser_success() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userInfo));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtils.createJWTAccessToken("username")).thenReturn("accessToken");
        when(jwtUtils.createJWTRefreshToken("username")).thenReturn("refreshToken");

        AuthResponse response = thesisAuthService.loginUser(authRequest, "fingerPrint");

        assertEquals("accessToken", response.getAccess());
        assertEquals("refreshToken", response.getRefresh());
    }

    @Test
    void loginUser_failure() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userInfo));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> thesisAuthService.loginUser(authRequest, "fingerPrint"));
    }

    @Test
    void registerUser_success() {
        when(userRepository.save(any(UserInfo.class))).thenReturn(userInfo);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtUtils.createJWTAccessToken("username")).thenReturn("accessToken");
        when(jwtUtils.createJWTRefreshToken("username")).thenReturn("refreshToken");

        AuthResponse response = thesisAuthService.registerUser(authRequest, "fingerPrint");

        assertEquals("accessToken", response.getAccess());
        assertEquals("refreshToken", response.getRefresh());
    }

    @Test
    void getUserByUsername_success() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(userInfo));

        UserResponse response = thesisAuthService.getUserByUsername("username");

        assertNotNull(response);
        assertEquals("username", response.getUsername());
    }

    @Test
    void getUserByUsername_failure() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> thesisAuthService.getUserByUsername("username"));
        assertEquals(NOT_FOUND, exception.getReason());
    }

    @Test
    void logoutUser_success() {
        when(accessTokenRepository.findByUsername("username")).thenReturn(List.of(accessToken));
        when(refreshTokenRepository.findByFingerPrint("fingerPrint")).thenReturn(List.of(refreshToken));

        thesisAuthService.logoutUser("username", "fingerPrint");

        verify(accessTokenRepository).saveAll(anyList());
        verify(refreshTokenRepository).saveAll(anyList());
    }

    @Test
    void refreshToken_success() {
        RefreshRequest refreshRequest = new RefreshRequest();
        when(refreshTokenRepository.findById("refreshToken")).thenReturn(Optional.of(refreshToken));
        when(jwtUtils.createJWTAccessToken("username")).thenReturn("newAccessToken");
        when(jwtUtils.createJWTRefreshToken("username")).thenReturn("newRefreshToken");

        AuthResponse response = thesisAuthService.refreshToken(refreshRequest, "fingerPrint");

        assertEquals("newAccessToken", response.getAccess());
        assertEquals("newRefreshToken", response.getRefresh());
    }

    @Test
    void refreshToken_failure() {
        RefreshRequest refreshRequest = new RefreshRequest();
        when(refreshTokenRepository.findById("invalidRefreshToken")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> thesisAuthService.refreshToken(refreshRequest, "fingerPrint"));
    }
}
