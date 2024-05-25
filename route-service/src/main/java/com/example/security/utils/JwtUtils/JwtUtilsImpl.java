package com.example.security.utils.JwtUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtilsImpl implements JwtUtils {
    private final Integer ACCESS_TIME;

    private final Integer REFRESH_TIME;

    private final Algorithm algorithm;

    public JwtUtilsImpl(@Value("${jwt.secret-key}") String SECRET_KEY, @Value("#{new Integer('${jwt.access-time}')}") Integer ACCESS_TIME, @Value("#{new Integer('${jwt.refresh-time}')}") Integer REFRESH_TIME) {
        this.ACCESS_TIME = ACCESS_TIME;
        this.REFRESH_TIME = REFRESH_TIME;
        this.algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

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
    public Optional<DecodedJWT> getDecodedJwt(String token) {
        try {
            return Optional.of(JWT.require(algorithm).build().verify(token));
        } catch (JWTVerificationException | NullPointerException ex) {
            return Optional.empty();
        }
    }

    @Override
    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    @Override
    public boolean isTokenExpired(DecodedJWT token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public boolean isTokenValid(DecodedJWT token) {
        try {
            getJWTVerifier().verify(token);
            return !isTokenExpired(token);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private JWTVerifier getJWTVerifier() {
        return JWT.require(algorithm).build();
    }

    private Date extractExpiration(DecodedJWT token) {
        return token.getExpiresAt();
    }
}
