package com.example.security.model;

import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.DigestUtils;

import java.util.Collection;

@ToString(callSuper = true)
@Setter
public class AuthenticationToken extends AbstractAuthenticationToken {

    @Setter
    private String username;

    public AuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = principal;
    }

    public void authenticate() {
        setAuthenticated(getDetails() != null && getDetails() instanceof SessionUser && !((SessionUser) getDetails()).hasExpired());
    }

    @Override
    public String getCredentials() {
        return "";
    }

    @Override
    public String getPrincipal() {
        return username != null ? username : "";
    }

    public String getHash() {
        return DigestUtils.md5DigestAsHex(String.format("%s_%d", username, ((SessionUser) getDetails()).getCreated().getTime()).getBytes());
    }

}
