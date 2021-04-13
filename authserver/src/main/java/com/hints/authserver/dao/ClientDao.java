package com.hints.authserver.dao;

import com.hints.authserver.model.Client;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
    @Autowired
    private Dao dao;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Client findByClientId(String clientId) {
        Client client = dao.fetch(Client.class, Cnd.where("clientId", "=", clientId));
        return client;
    }

}
