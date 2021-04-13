package com.hints.clientone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableResourceServer //这是一台资源服务器
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启方法级别保护
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "resource1";

    @Autowired
    private CustomizeAccessDecisionManager accessDecisionManager;

    @Autowired
    private CustomizeFilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomizeAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
                        return o;
                    }
                });
        http.
                exceptionHandling()
                //用来解决认证过的用户访问无权限资源时的异常
                .accessDeniedHandler(customAccessDeniedHandler)
                //用来解决匿名用户访问无权限资源时的异常
                .authenticationEntryPoint(customAuthenticationEntryPoint);
    }
}
