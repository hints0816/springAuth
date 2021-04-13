package com.hints.clientone.dao;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    Dao dao;

    public Record select(String username) {
        Record re = dao.fetch("userinfo", Cnd.where("name", "=", username));
        System.out.println(re);
        return re;
    }
}
