package com.hints.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomizeAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/order/**","/oauth/index")
                .authenticated(); // 配置order访问控制，必须认证后才可以访问
        http.
        exceptionHandling()
                //用来解决认证过的用户访问无权限资源时的异常
                .accessDeniedHandler(customAccessDeniedHandler)
                //用来解决匿名用户访问无权限资源时的异常
                .authenticationEntryPoint(customAuthenticationEntryPoint);
    }
}
