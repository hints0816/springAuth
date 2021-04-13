package com.hints.authserver.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/*该类主要是封装用户的认证*/

public class CustomizeUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 500L;
    private final Object principal;
    private Object credentials;
    private Object company;


    public CustomizeUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object company) {
        super((Collection)null);
        this.principal = principal; //认证信息
        this.credentials = credentials;
        this.company = company;
        this.setAuthenticated(false);
    }

    public CustomizeUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object company, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.company = company;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public Object getCompany() {
        return this.company;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}

