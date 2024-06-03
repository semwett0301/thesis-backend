package com.example.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.model.AuthenticationToken;
import com.example.security.utils.JwtUtils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
@Setter
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication resultToken = new AuthenticationToken(Optional.empty(), Optional.empty(), new ArrayList<>());

        try {
            Optional<DecodedJWT> accessToken = getAccessToken(request);

            if (accessToken.isPresent()) {
                final var jwt = accessToken.get();
                final var username = jwtUtils.extractUsername(jwt);

                final var authToken = new AuthenticationToken(Optional.of(username), Optional.of(jwt), new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                resultToken = authenticationManager.authenticate(authToken);
            }
        } catch (Exception e) {
            log.info("Token expired");
        } finally {
            SecurityContextHolder.getContext().setAuthentication(resultToken);
            doFilter(request, response, filterChain);
        }
    }

    private Optional<DecodedJWT> getAccessToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);

            return jwtUtils.getDecodedJwt(token);
        }

        return Optional.empty();
    }
}
