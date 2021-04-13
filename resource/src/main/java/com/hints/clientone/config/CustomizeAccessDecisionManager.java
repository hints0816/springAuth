package com.hints.clientone.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomizeAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, AuthenticationException {
        Iterator<ConfigAttribute> iterator = collection.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute ca = iterator.next();
            //当前请求需要的权限
            String needRole = ca.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                /*if (authentication instanceof AnonymousAuthenticationToken) {*/
                    throw new BadCredentialsException("未登录");
                /*} else
                    return;*/
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            //当前用户所具有的权限
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
                    throw new InsufficientAuthenticationException("当前访问为匿名访问，请联系管理员!");
                } else if(needRole.trim().equals("GSCMAD")) {
                    return;
                } else if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
