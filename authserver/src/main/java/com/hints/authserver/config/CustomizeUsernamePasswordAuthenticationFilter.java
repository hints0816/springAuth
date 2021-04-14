package com.hints.authserver.config;

import com.hints.authserver.constant.SecurityConstants;
import com.hints.authserver.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String verifycodeParameter = "verifycode";
    private String uuidParameter = "uuid";
    private boolean postOnly = true;

    @Autowired
    private RedisCache redisCache;

    public CustomizeUsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/oauth3/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String verifycode = this.obtainVerifycode(request);
            String uuid = this.obtainUUID(request);
            String verifyKey = SecurityConstants.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                throw new AuthenticationServiceException("error captcha");
            }
            if (!verifycode.equalsIgnoreCase(captcha))
            {
                throw new AuthenticationServiceException("error captcha");
            }
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            if (verifycode == null) {
                verifycode = "";
            }

            if (uuid == null) {
                uuid = "";
            }

            username = username.trim();
            CustomizeUsernamePasswordAuthenticationToken authRequest = new CustomizeUsernamePasswordAuthenticationToken(username, password, verifycode);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected String obtainVerifycode(HttpServletRequest request) {
        return request.getParameter(this.verifycodeParameter);
    }

    protected String obtainUUID(HttpServletRequest request) {
        return request.getParameter(this.uuidParameter);
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

    public void setVerifycodeParameter(String verifycodeParameter) {
        Assert.hasText(verifycodeParameter, "verifycode parameter must not be empty or null");
        this.verifycodeParameter = verifycodeParameter;
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

    public final String getVerifycodeParameter() {
        return this.verifycodeParameter;
    }
}
