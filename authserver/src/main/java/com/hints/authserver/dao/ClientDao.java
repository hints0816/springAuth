package com.hints.authserver.dao;

import com.hints.authserver.model.Client;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDao {
    @Autowired
    private Dao dao;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Client findByClientId(String clientId) {
        Client client = dao.fetch(Client.class, Cnd.where("client_id", "=", clientId));
        return client;
    }

    public List<Client> queryClient(Client client) {
        Cnd cnd = Cnd.from(dao,client);
        List<Client> query = dao.query(Client.class, cnd);
        return query;
    }



}
