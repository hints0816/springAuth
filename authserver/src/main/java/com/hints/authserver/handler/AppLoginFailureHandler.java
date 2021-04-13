package com.hints.authserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("appLoginFailureHandler")
@Slf4j
public class AppLoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = savedRequest.getRedirectUrl();
        String param = targetUrl.substring(targetUrl.indexOf('?'));
        String defaultFailureUrl = "/oauth2/authorize"+param;

        response.setContentType("application/json;charset=UTF-8");
        if(exception instanceof BadCredentialsException){
            String message = "用户名或密码错误";
            request.setAttribute("error",message);
            request.getRequestDispatcher("/oauth2/error").forward(request,response);
        }
    }
}
