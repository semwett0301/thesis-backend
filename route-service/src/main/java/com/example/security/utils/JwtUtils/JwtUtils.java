package com.example.security.utils.JwtUtils;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

public interface JwtUtils {
    String createJWTAccessToken(String username);

    String createJWTRefreshToken(String username);

    UsernamePasswordAuthenticationToken getUsernameAuthTokenFromJwt(DecodedJWT jwtToken);

    Optional<DecodedJWT> getDecodedJwt(String token);
}
