package com.hints.authserver.config;

import com.hints.authserver.handler.AppLoginFailureHandler;
import com.hints.authserver.handler.SecAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
/*
@EnableWebSecurity//不需要
*/
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceDetail userServiceDetail;

    @Autowired
    private SecAuthenticationSuccessHandler secAuthenticationSuccessHandler;

    @Autowired
    private AppLoginFailureHandler appLoginFailureHandler;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(new BCryptPasswordEncoder());
    }

    //密码编码器
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/icon/**", "/img/**", "/js/**", "/favicon.ico");
    }

    //安全拦截机制,什么时候关闭csrf
    //这里定义了oauth2拦截哪些url
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/users/current", "/oauth2/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        http
                .formLogin()
                .loginPage("/oauth/index") //所有拦截下来的URL都会到这个地址，默认为/login，登出的默认url为logout
                .loginProcessingUrl("/oauth/login")
                .failureHandler(appLoginFailureHandler)
                .successHandler(secAuthenticationSuccessHandler)
                .usernameParameter("username")
                .passwordParameter("password");
        http
                .addFilterAt(lindAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests();
        http.logout();
    }

    @Bean
    CustomizeUsernamePasswordAuthenticationFilter lindAuthenticationFilter() {
        CustomizeUsernamePasswordAuthenticationFilter customizeUsernamePasswordAuthenticationFilter = new CustomizeUsernamePasswordAuthenticationFilter();
        ProviderManager providerManager =
                new ProviderManager(Collections.singletonList(customAuthenticationProvider));
        customizeUsernamePasswordAuthenticationFilter.setAuthenticationManager(providerManager);
        customizeUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(secAuthenticationSuccessHandler);
        customizeUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(appLoginFailureHandler);
        return customizeUsernamePasswordAuthenticationFilter;
    }
}
