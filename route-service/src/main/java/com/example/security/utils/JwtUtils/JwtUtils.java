package com.example.security.utils.JwtUtils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.model.AuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

public interface JwtUtils {
    String createJWTAccessToken(String username);

    String createJWTRefreshToken(String username);

    Optional<DecodedJWT> getDecodedJwt(String token);

    String extractUsername(DecodedJWT token);

    boolean isTokenExpired(DecodedJWT token);

    boolean isTokenValid(DecodedJWT token);
}
