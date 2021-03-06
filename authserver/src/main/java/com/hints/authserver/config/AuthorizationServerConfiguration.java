package com.hints.authserver.config;

import com.hints.authserver.service.ClientDetailsServiceImpl;
import com.hints.authserver.service.UserServiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer //?????????????????????????????????
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceDetail userServiceDetail;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomOAuth2RequestFactory customOAuth2RequestFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //1.??????????????????????????????
        ClientDetailsServiceImpl clientDetailsService = new ClientDetailsServiceImpl();
        clientDetailsService.setRedisTemplate(redisTemplate);
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //Token????????????????????????
                .tokenStore(jwtTokenStore)
                //WebSecurity????????????
                .authenticationManager(authenticationManager)
                //???????????????????????????
                .userDetailsService(userServiceDetail)
                .authorizationCodeServices(authorizationCodeServices())
                .requestFactory(customOAuth2RequestFactory);
        //???????????????????????????(payload)
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(JwtTokenEnhancer(), jwtTokenConfig.jwtAccessTokenConverter()));
        endpoints
                .accessTokenConverter(jwtTokenConfig.jwtAccessTokenConverter())
                .tokenEnhancer(enhancerChain)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //?????????????????????
                .pathMapping("/oauth/confirm_access", "/custom/confirm_access");

        endpoints.tokenGranter(new CompositeTokenGranter(getTokenGranters(endpoints)));
    }

    //???????????????????????????
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // ??????????????????
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")//?????????token?????????????????????
                .checkTokenAccess("isAuthenticated()")//????????????token???????????????
        ;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        JdbcAuthorizationCodeServices authorizationCodeServices = new CustomJdbcAuthorizationCodeServices(dataSource);
        return authorizationCodeServices;
    }

    @Bean
    public TokenEnhancer JwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    private List<TokenGranter> getTokenGranters(AuthorizationServerEndpointsConfigurer endpoints){
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();
        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetails, requestFactory));//???????????????????????????????????????????????????????????????????????????
            tokenGranters.add(new CustomTokenGranter(new ProviderManager(getProvider(),null),tokenServices,endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory()));
        }
        //???????????????granter
        return tokenGranters;
    }

    private List<AuthenticationProvider> getProvider(){
        List<AuthenticationProvider> list=new ArrayList<AuthenticationProvider>();
        list.add(customAuthenticationProvider);
        return list;
    }
}
