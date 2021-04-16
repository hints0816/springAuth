package com.hints.client.config;

import com.hints.client.utils.HTTPUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        if (HTTPUtils.isAjaxRequest(httpServletRequest)) {// AJAX请求,使用response发送403
            httpServletResponse.sendError(401);
        } else if (!httpServletResponse.isCommitted()) {// 非AJAX请求，跳转系统默认的403错误界面，在web.xml中配置
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}