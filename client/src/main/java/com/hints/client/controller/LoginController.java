package com.hints.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/oauth/login")
    public String loginPage(){
        return "base-login";
    }


    @GetMapping("/oauth/register")
    public String registerPage(){
        return "base-register";
    }
}
