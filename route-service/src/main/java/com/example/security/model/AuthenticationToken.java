package com.example.security.model;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.DigestUtils;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.Optional;

@ToString(callSuper = true)
@Setter
public class AuthenticationToken extends AbstractAuthenticationToken {

    @Setter
    private Optional<String> username;

    @Setter
    private Optional<DecodedJWT> token;

    public AuthenticationToken(Optional<String> principal, Optional<DecodedJWT> token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = principal;
        this.token = token;
    }

    @Override
    public Optional<DecodedJWT> getCredentials() {
        return this.token;
    }

    @Override
    public Optional<String> getPrincipal() {
        return username;
    }
}
