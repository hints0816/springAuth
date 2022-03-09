package com.hints.authserver.model;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.time.LocalDateTime;

@Data
@Table("sys_client")
public class Client {
    /**
     * 客户端id
     */
    @Name
    private String client_id;

    /**
     * 客户端秘钥
     */
    @Column(hump = true)
    private String clientSecret;

    /**
     * 授权码模式
     */
    @Column(hump = true)
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @Column(hump = true)
    private String redirectUri;

    /**
     * TOKEN失效时长（秒）
     */
    @Column(hump = true)
    private Double accessTokenValiditySeconds;

    /**
     * REFRESH_TOKEN失效时长（秒）
     */
    @Column(hump = true)
    private Double refreshTokenValiditySeconds;

    /**
     * 作用域
     */
    @Column(hump = true)
    private String scopes;

    /**
     * 是否授权页面
     */
    @Column(hump = true)
    private String autoApproveScopes;

    /**
     * 资源id
     */
    @Column(hump = true)
    private String resourceId;

    /**
     * 资源id
     */
    @Column(hump = true)
    private String clientName;

    /**
     * 资源id
     */
    @Column(hump = true)
    private Integer applicationType;

    /**
     * 资源id
     */
    @Column(hump = true)
    private String introduction;

    /**
     * 资源id
     */
    @Column(hump = true)
    private LocalDateTime createTime;

    /**
     * 资源id
     */
    @Column(hump = true)
    private LocalDateTime updateTime;
}