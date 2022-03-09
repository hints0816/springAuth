package com.hints.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.hints.authserver.dao.ClientDao;
import com.hints.authserver.dao.UserDao;
import com.hints.authserver.model.Client;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @PutMapping(value = "/oauth2/client/update")
    public Map edit(@RequestBody Client client) {
        LocalDateTime createTime = clientDao.findByClientId(client.getClient_id()).getCreateTime();
        client.setCreateTime(createTime);
        client.setUpdateTime(LocalDateTime.now());
        int i = clientDao.updateClient(client);
        return i>0?NutMap.NEW().addv("code",200):NutMap.NEW().addv("code",500);
    }

    @DeleteMapping(value = "/oauth2/client/del")
    public Map delete(@RequestParam(value = "client_id")String clientID) {
        int i = clientDao.deleteClient(clientID);
        return i>0?NutMap.NEW().addv("code",200):NutMap.NEW().addv("code",500);
    }

    @PostMapping(value = "/oauth2/client/register")
    public Map register(@RequestBody Client client) {
        String clientID = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        client.setClient_id(clientID);
        client.setAuthorizedGrantTypes("authorization_code,refresh_token");
        client.setAutoApproveScopes("0");
        String clientSecret = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String hashed = BCrypt.hashpw(clientSecret, BCrypt.gensalt());
        clientSecret = "{bcrypt}"+hashed;
        client.setClientSecret(clientSecret);
        client.setResourceId("resource1");
        client.setScopes("getUserInfo");
        client.setCreateTime(LocalDateTime.now());
        client.setAccessTokenValiditySeconds(1800D);
        client.setRefreshTokenValiditySeconds(1800D);
        int i = clientDao.insertClient(client);
        return i>0?NutMap.NEW().addv("code",200).addv("clientSecret",clientSecret):NutMap.NEW().addv("code",500);
    }
}
