/*
package com.hints.clientone.controller;

import com.hints.clientone.dao.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
public class UserController {
    @Autowired
    UserDao userDao;

    @GetMapping("hello")
    public Object find() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        Record re = userDao.select("hints");
        return re;
    }

    @GetMapping("user/{id}")
    public String getUsers(@PathVariable("id") String id){
        System.out.println("12");
        return "123";
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

}
*/
