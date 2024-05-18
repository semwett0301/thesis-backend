package com.example.security.utils.JwtUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtilsImpl implements JwtUtils {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-time}")
    private Integer ACCESS_TIME;

    @Value("${jwt.refresh-time}")
    private Integer REFRESH_TIME;

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    public String createJWTAccessToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TIME))
                .sign(algorithm);
    }

    @Override
    public String createJWTRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .sign(algorithm);
    }

    @Override
    public UsernamePasswordAuthenticationToken getUsernameAuthTokenFromJwt(DecodedJWT jwtToken) {
        return new UsernamePasswordAuthenticationToken(jwtToken.getSubject(), Optional.empty());
    }

    @Override
    public Optional<DecodedJWT> getDecodedJwt(String token) {
        try {
            return Optional.of(JWT.require(algorithm).build().verify(token));
        } catch (JWTVerificationException | NullPointerException ex) {
            return Optional.empty();
        }
    }
}
