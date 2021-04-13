package com.hints.authserver.model;

import lombok.Data;
import org.nutz.dao.entity.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Table("sys_user")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;
    /** 用户ID */
    @Id
    private Long user_id;
    /** 用户账号 */
    @Name
    private String user_name;
    /** 用户昵称 */
    private String nick_name;
    /** 用户邮箱 */
    private String email;
    /** 手机号码 */
    private String phonenumber;
    /** 用户性别 */
    private String sex;
    /** 用户头像 */
    private String avatar;
    /** 密码 */
    private String password;
    /** 盐加密 */
    private String salt;
    /** 帐号状态（0正常 1停用） */
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String del_flag;
    /** 最后登录IP */
    private String login_ip;
    /** 最后登录时间 */
    private Date login_date;
    /** 角色对象 */
    private List<Role> roles;
    /** 角色组 */
    private Long[] role_ids;
    /** 岗位组 */
    private Long[] post_ids;

    @ManyMany(target = Role.class,
            relation = "sys_user_role",
            from = "user_id",
            to = "role_id")
    private List<Role> utype;

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return utype;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user_name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
