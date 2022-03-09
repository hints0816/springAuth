package com.hints.authserver.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/*该类主要是封装用户的认证(自定义)*/
public class CustomizeUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 500L;
    private final Object principal;
    private Object credentials;
    private Object verifycode;


    public CustomizeUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object verifycode) {
        super((Collection)null);
        this.principal = principal; //认证信息
        this.credentials = credentials;
        this.verifycode = verifycode;
        this.setAuthenticated(false);
    }

    public CustomizeUsernamePasswordAuthenticationToken(Object principal, Object credentials, Object verifycode, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.verifycode = verifycode;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public Object getVerifycode() {
        return this.verifycode;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}

