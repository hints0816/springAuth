package com.hints.authserver.config;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 180686 on 2021/4/14 15:49
 */

@Component
public class CustomOAuth2RequestFactory extends DefaultOAuth2RequestFactory {
    private final ClientDetailsService customClientDetailsService;
    private SecurityContextAccessor securityContextAccessor = new DefaultSecurityContextAccessor();
    private boolean checkUserScopes = false;

    public CustomOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
        this.customClientDetailsService = clientDetailsService;
    }

    public void setSecurityContextAccessor(SecurityContextAccessor securityContextAccessor) {
        this.securityContextAccessor = securityContextAccessor;
    }

    @Override
    public AuthorizationRequest createAuthorizationRequest(Map<String, String> authorizationParameters) {
        String clientId = (String)authorizationParameters.get("client_id");
        String state = (String)authorizationParameters.get("state");
        String redirectUri = (String)authorizationParameters.get("redirect_uri");
        Set<String> responseTypes = OAuth2Utils.parseParameterList((String)authorizationParameters.get("response_type"));
        Set<String> scopes = this.extractScopes(authorizationParameters, clientId);
        AuthorizationRequest request = new AuthorizationRequest(authorizationParameters, Collections.emptyMap(), clientId, scopes, (Set)null, (Collection)null, false, state, redirectUri, responseTypes);
        ClientDetails clientDetails = this.customClientDetailsService.loadClientByClientId(clientId);
        request.setResourceIdsAndAuthoritiesFromClientDetails(clientDetails);
        return request;
    }

    private Set<String> extractScopes(Map<String, String> requestParameters, String clientId) {
        Set<String> scopes = OAuth2Utils.parseParameterList((String)requestParameters.get("scope"));
        ClientDetails clientDetails = this.customClientDetailsService.loadClientByClientId(clientId);
        if (scopes == null || scopes.isEmpty()) {
            Set<String> newscopes = new HashSet<>();
            newscopes.add("getuserinfo");
            scopes = newscopes;
        }

        if (this.checkUserScopes) {
            scopes = this.checkUserScopes(scopes, clientDetails);
        }

        return scopes;
    }

    private Set<String> checkUserScopes(Set<String> scopes, ClientDetails clientDetails) {
        if (!this.securityContextAccessor.isUser()) {
            return scopes;
        } else {
            Set<String> result = new LinkedHashSet();
            Set<String> authorities = AuthorityUtils.authorityListToSet(this.securityContextAccessor.getAuthorities());
            Iterator var5 = scopes.iterator();

            while(true) {
                String scope;
                do {
                    if (!var5.hasNext()) {
                        return result;
                    }

                    scope = (String)var5.next();
                } while(!authorities.contains(scope) && !authorities.contains(scope.toUpperCase()) && !authorities.contains("ROLE_" + scope.toUpperCase()));

                result.add(scope);
            }
        }
    }
}
