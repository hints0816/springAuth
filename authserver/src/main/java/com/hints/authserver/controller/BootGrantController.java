package com.hints.authserver.controller;

import com.hints.authserver.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
// 必须配置
@SessionAttributes("authorizationRequest")
public class BootGrantController {

    @Autowired
    UserDao userDao;

    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("base-grant");
        String clientname = userDao.getclientname(authorizationRequest.getClientId());
        view.addObject("clientId", clientname);
        return view;
    }
}
