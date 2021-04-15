package com.hints.resource.config;

import com.hints.resource.utils.HTTPUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决认证过的用户访问无权限资源时的异常
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse resp, AccessDeniedException e) throws IOException, ServletException {
        if (HTTPUtils.isAjaxRequest(httpServletRequest)) {// AJAX请求,使用response发送403
            resp.sendError(403);
        } else if (!resp.isCommitted()) {// 非AJAX请求，跳转系统默认的403错误界面，在web.xml中配置
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}