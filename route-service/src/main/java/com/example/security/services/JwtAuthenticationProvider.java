package com.example.security.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.repositories.redis.AccessTokenRepository;
import com.example.security.model.AuthenticationToken;
import com.example.security.model.Role;
import com.example.security.utils.JwtUtils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private AccessTokenRepository accessTokenRepository;
    private JwtUtils jwtUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var tokenOptional = (Optional<DecodedJWT>) authentication.getCredentials();

        if (tokenOptional.isPresent()) {
            final var token = tokenOptional.get();
            final var username = jwtUtils.extractUsername(token);
            final var tokenEntity = accessTokenRepository.findById(token.getToken());

            final List<GrantedAuthority> authorities = new ArrayList<>();

            if (tokenEntity.isPresent()) {
                if (tokenEntity.get().getIsActive() && jwtUtils.isTokenValid(token))
                    authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));

                return new AuthenticationToken(Optional.of(username), Optional.of(token), authorities);
            }
        }


        throw new AuthenticationException("Token is invalid") {
        };
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
