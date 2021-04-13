package com.hints.authserver.config;

import com.hints.authserver.dao.UserDao;
import com.hints.authserver.model.Role;
import com.hints.authserver.model.User;
import org.nutz.dao.entity.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceDetail implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserServiceDetail.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.finduser(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(username))
                .build();
        return userDetails;
    }

    public UserDetails loadUserByUsernameAndCompany(String username,String company) throws UsernameNotFoundException {
        User user = userDao.finduser(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在!");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(username))
                .build();
        logger.info("company:"+company);
        return userDetails;
    }

    //查询该用户的权限集
    public Collection<? extends GrantedAuthority> getAuthorities(String username) {
        List<Role> list1 = new ArrayList<>();
        List<Record> list = userDao.getAuth(username);
        for (Map map : list) {
            Role role = new Role();
            role.setRole_id(Long.valueOf(map.get("role_id").toString()));
            role.setRole_name(map.get("role_name").toString());
            list1.add(role);
    }
        return list1;
    }
}