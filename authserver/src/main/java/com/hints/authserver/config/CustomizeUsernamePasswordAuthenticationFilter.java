package com.hints.authserver.config;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomizeUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private String companyParameter = "company";
    private boolean postOnly = true;

    public CustomizeUsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/ccc/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String comp = this.obtainComp(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            if (comp == null) {
                comp = "";
            }

            username = username.trim();
            CustomizeUsernamePasswordAuthenticationToken authRequest = new CustomizeUsernamePasswordAuthenticationToken(username, password, comp);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    protected String obtainComp(HttpServletRequest request) {
        return request.getParameter(this.companyParameter);
    }

    protected void setDetails(HttpServletRequest request, CustomizeUsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setCompanyParameter(String companyParameter) {
        Assert.hasText(companyParameter, "Company parameter must not be empty or null");
        this.companyParameter = companyParameter;
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }

    public final String getCompanyParameter() {
        return this.companyParameter;
    }
}
