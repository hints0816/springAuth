package com.hints.authserver.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "custommode";

    private final AuthenticationManager authenticationManager;

    public CustomTokenGranter(AuthenticationManager authenticationManager,
                              AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    protected CustomTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                 ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    /**
     * 在这个方法可以进行验证码等其他操作
     * @param client
     * @param tokenRequest
     * @return
     */
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
        String username = (String)parameters.get("username");
        String password = (String)parameters.get("password");
        try {
            password = new String(parameters.get("password").getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String comp = (String)parameters.get("comp");
        parameters.remove("password");
        parameters.remove("comp");

        Authentication userAuth = new CustomizeUsernamePasswordAuthenticationToken(username, password,comp);
        ((AbstractAuthenticationToken)userAuth).setDetails(parameters);
        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException var8) {
            throw new InvalidGrantException(var8.getMessage());
        } catch (BadCredentialsException var9) {
            throw new InvalidGrantException(var9.getMessage());
        }

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }
    }
}