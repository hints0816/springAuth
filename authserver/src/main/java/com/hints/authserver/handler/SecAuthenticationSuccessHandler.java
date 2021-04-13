package com.hints.authserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("secAuthenticationSuccessHandler")
@Slf4j
public class SecAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("登录成功");
        /*底层会从request里拿到session，再从session拿出原来要跳转的地址进行跳转*/
        /*super.onAuthenticationSuccess(request, response, authentication);*/
        String header = request.getHeader("Authorization");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            String targetUrl = savedRequest.getRedirectUrl();
            response.setContentType("application/json;charset=UTF-8");
            request.setAttribute("targetUrl",targetUrl);
            request.getRequestDispatcher("/oauth2/success").forward(request,response);
        }
    }
}
