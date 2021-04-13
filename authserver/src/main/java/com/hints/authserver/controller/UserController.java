package com.hints.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.hints.authserver.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/users/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }
}
