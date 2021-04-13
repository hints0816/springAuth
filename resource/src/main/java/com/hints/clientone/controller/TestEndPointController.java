package com.hints.clientone.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
public class TestEndPointController {

    Logger logger = LoggerFactory.getLogger(TestEndPointController.class);

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.toString());
        return "product id : " + id;
    }

//    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/oauth/{id}")
    public String getOrder(@PathVariable String id) {
        //接口获取token的方式
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication.toString());
        return "order id : " + id;
    }

    @GetMapping("/getPrinciple")
    public OAuth2Authentication getPrinciple(OAuth2Authentication oAuth2Authentication, Principal principal, Authentication authentication) {
        logger.info(oAuth2Authentication.getUserAuthentication().getAuthorities().toString());
        logger.info(oAuth2Authentication.toString());
        logger.info("principal.toString()"+principal.toString());
        logger.info("principal.getName()"+principal.getName());
        logger.info("authentication:"+authentication.getAuthorities().toString());
        return oAuth2Authentication;
    }

    @GetMapping("/me")
    public Object getCurrentUser1(Authentication authentication, HttpServletRequest request) throws UnsupportedEncodingException {
        String header = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(header, "Bearer ");
        Claims claims = Jwts.parser()
                .setSigningKey("internet_plus".getBytes("utf-8"))
                .parseClaimsJws(token).getBody();
        String userId = String.valueOf(claims.get("provider"));
        System.out.println(userId);
        return claims;
    }

    @GetMapping(value = "get")
    public Object get(Authentication authentication){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getCredentials();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        String token = details.getTokenValue();
        return token;
    }

}
