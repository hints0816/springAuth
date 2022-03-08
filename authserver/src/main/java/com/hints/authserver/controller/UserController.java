package com.hints.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.hints.authserver.dao.ClientDao;
import com.hints.authserver.dao.UserDao;
import com.hints.authserver.model.Client;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @Autowired
    ClientDao clientDao;


    @RequestMapping(value = "/users/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/oauth2/client/list", method = RequestMethod.GET)
    public List<Client> listClient(Client client) {
        List<Client> clients = clientDao.queryClient(client);
        return clients;
    }

}
