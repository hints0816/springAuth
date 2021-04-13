package com.hints.clientone.config;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

@Component
public class CustomizeFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //进来都是调用auth2模块通过用户验证的请求
        //获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

           /* String[] values = new String[1];
            values[0] = "GSCMAD";
            return SecurityConfig.createList(values);*/
        return null;
        //没有匹配上的资源，都是登录访问
//        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

    //TODO String... antPatterns
    private boolean customMatcher(String requestUrl,String... antPatterns) {
        boolean result= false;
        String[] var4 = antPatterns;
        int var5 = antPatterns.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            String pattern = var4[var6];
            result = antPathMatcher.match(pattern,requestUrl);
            if(result){
                break;
            }
        }
        return result;

    }


}
