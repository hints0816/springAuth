package com.hints.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.hints.authserver.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@SessionAttributes("authorizationRequest")
public class BaseMainController {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TokenStore tokenStore;
    @Autowired
    private UserDao userDao;

    @RequestMapping("/oauth/index")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String c = (String) request.getAttribute("error");
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (null != savedRequest) {
            String targetUrl = savedRequest.getRedirectUrl();
            String param = "";
            if(targetUrl.contains("?")){
                param = targetUrl.substring(targetUrl.indexOf('?'));
            }
            if(param.contains("redirect_uri=http://localhost:8878/")){
                redirectStrategy.sendRedirect(request, response, "/oauth2/login");
            }else{
                System.out.println("????????????????????????:" + targetUrl);
                //??????????????????????????????url???????????????targetUrl???????????????
                redirectStrategy.sendRedirect(request, response, "/oauth2/authorize"+param);
            }
        }
        //??????????????????????????????
        return new HashMap() {{
            put("code", 401);
            put("msg", "???????????????????????????????????????????????????????????????");
        }};
    }

    @RequestMapping("/oauth2/setting")
    public ModelAndView toSetting() {
        ModelAndView model = new ModelAndView("base-setting");
        return model;
    }

    @RequestMapping(value = "/oauth2/authorize")
    public ModelAndView oauthLogin(Model model2, @RequestParam("response_type") String response_type,
                                   @RequestParam("redirect_uri") String redirect_uri,
                                   @RequestParam("client_id") String client_id, HttpServletResponse response, HttpServletRequest request){
        ModelAndView model = new ModelAndView("base-login");
        if(request.getAttribute("error")!=null){
            model.addObject("error",request.getAttribute("error"));
        }
        model.addObject("client_name",userDao.getclientname(client_id));
        return model;
    }

    @RequestMapping(value = "/oauth2/login")
    public ModelAndView login(HttpServletResponse response, HttpServletRequest request){
        ModelAndView model = new ModelAndView("base-login");
        return model;
    }

    @RequestMapping(value = "/oauth2/error")
    @ResponseBody
    public JSONObject loginError(HttpServletResponse response, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error",(String) request.getAttribute("error"));
        return jsonObject;
    }

    @RequestMapping(value = "/oauth2/success")
    @ResponseBody
    public JSONObject loginSuccess(HttpServletResponse response, HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("targetUrl",(String) request.getAttribute("targetUrl"));
        return jsonObject;
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {

    }
}