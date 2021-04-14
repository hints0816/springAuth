package com.hints.client.controller;

import com.hints.client.utils.RedisCache;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class ClientController {
    String clientId = null;
    String clientSecret = null;
    String accessTokenUrl = null;
    String userInfoUrl = null;
    String redirectUrl = null;
    String response_type = null;
    String code= null;
    String state= null;
    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisCache redisCache;

    public static final String STATE_KEY = "state:";

    @RequestMapping("/requestServerCode")
    public String requestServerCode(HttpServletRequest request, HttpServletResponse response)
            throws OAuthProblemException {
        clientId = "client_3";
        accessTokenUrl = "authorize";
        redirectUrl = "http://localhost:8765/oauth/callbackCode";
        response_type = "code";
        String requestUrl = null;
        try {
            String state = UUID.randomUUID().toString().replace("-", "").toLowerCase();

            //构建oauth的请求。设置授权服务地址（accessTokenUrl）、clientId、response_type、redirectUrl
            OAuthClientRequest accessTokenRequest = OAuthClientRequest
                    .authorizationLocation(accessTokenUrl)
                    .setResponseType(response_type)
                    .setClientId(clientId)
                    .setRedirectURI(redirectUrl)
                    .setState(state)
                    //该build函数内scope设置多个时使用+号分隔
                    /*.setScope("getuserinfo")*/
                    .buildQueryMessage();
            redisCache.setCacheObject(STATE_KEY+state,1,10, TimeUnit.MINUTES);
            requestUrl = accessTokenRequest.getLocationUri();
            System.out.println("获取授权码方法中的requestUrl的值----"+requestUrl);
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }
        return "redirect:http://localhost:6001/oauth/"+requestUrl ;
    }

    //如何解决前端跨域问题，anyway，what's cross
    @CrossOrigin
    @RequestMapping("/oauth/callbackCode")
    public void toLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        clientId = "client_3";
        clientSecret = "123456";
        accessTokenUrl = "http://localhost:6001/oauth/token";
        userInfoUrl = "userInfoUrl";
        redirectUrl = "http://localhost:8765/oauth/callbackCode";
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        code = httpRequest.getParameter("code");
        state = httpRequest.getParameter("state");
        if(redisCache.getCacheObject(STATE_KEY+state)!=null) {
            redisCache.deleteObject(STATE_KEY + state);
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            try {
                OAuthClientRequest accessTokenRequest = OAuthClientRequest
                        .tokenLocation(accessTokenUrl)
                        .setGrantType(GrantType.AUTHORIZATION_CODE)
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setCode(code)
                        .setRedirectURI(redirectUrl)
                        .buildQueryMessage();
                OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
                String accessToken = oAuthResponse.getAccessToken();
                String refreshToken = oAuthResponse.getRefreshToken();
                Long expiresIn = oAuthResponse.getExpiresIn();
               /* OAuthClientRequest bearerClientRequest =  new OAuthBearerClientRequest(ConstantKey.OAUTH_CLIENT_GET_RESOURCE)
                        .setAccessToken(accessToken).buildQueryMessage();
                OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse. class );
                String resBody = resourceResponse.getBody();*/
                logger.info("accessToken: " + accessToken + " refreshToken: " + refreshToken + " expiresIn: " + expiresIn);
                /*return "redirect:http://localhost:8765/oauth/getToken?accessToken="+accessToken;*/
                Cookie cookies = new Cookie("Authorization", accessToken);
                cookies.setPath("/");
                cookies.setMaxAge(31536000);
                cookies.setHttpOnly(true);
                response.addCookie(cookies);
                response.addHeader("Authorization", "Bearer " + accessToken);
                response.setHeader("Access-Control-Expose-Headers", "Authorization");
                response.sendRedirect("http://localhost:8765/oauth/index");
            } catch (OAuthSystemException e) {
                e.printStackTrace();
            }
        }
    }

    /*此时客户端已经获得授权之后的token，
    此时想法是将token加到header上，
    请求资源服务器，
    此时在8765访问8876就会出现跨域访问的问题：注：跨端口*/

    /*这里可以这样举个例子：csdn拿着wechat的token去访问WeChat的用户信息*/
    @CrossOrigin
    @RequestMapping("/oauth/getToken")
    @ResponseBody
    public void getToken(HttpServletRequest request, HttpServletResponse response){
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        code = httpRequest.getParameter("accessToken");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+code);
        HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
        ResponseEntity<String> result = restTemplate.exchange("http://localhost:8876/product/1", HttpMethod.GET,entity,String.class);

        String clientname = result.getBody().toString();
        try {
            response.addHeader("Authorization", "Bearer " + code);
            response.sendRedirect("http://localhost:8765/oauth/index");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @CrossOrigin
    @RequestMapping("/oauth/index")
    public ModelAndView loginIndex(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView();
        view.setViewName("base-index");
        return view;
    }

}
