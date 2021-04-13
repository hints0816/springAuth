package com.hints.authserver.dao;

import com.hints.authserver.model.User;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {
    @Autowired
    private Dao dao;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User finduser(String userid) {
        User user = dao.fetch(User.class, userid);
        return user;
    }

    public List<Record> getAuth(String username) {
        Sql sql = Sqls.create("select t2.role_key from $table1 t1,$table2 t2,$table3 t3 " +
                "where t1.user_id = t3.user_id and t2.role_id = t3.role_id and t1.user_name = @username");

        sql.setVar("table1", "sys_user")
                .setVar("table2", "sys_role")
                .setVar("table3", "sys_user_role")
                .setParam("username", username);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(Record.class));
        List<Record> record = dao.execute(sql).getList(Record.class);
        return record;
    }

    public void createUser(String userid, String username, String password) {
        User user = new User();
        user.setUser_id(Long.valueOf(userid));
        String hash = encoder.encode(password);
        user.setPassword(hash);
        user.setUser_name(username);
        dao.insert(user);
    }

    public String getclientname(String clientid) {
        if("client_3".equals(clientid)){
            return "格力G平台";
        }
        return "";
    }
}
