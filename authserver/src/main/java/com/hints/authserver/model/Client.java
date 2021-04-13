package com.hints.authserver.model;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("sys_client")
public class Client {
    /**
     * 客户端id
     */
    @Name
    private String clientId;

    /**
     * 客户端秘钥
     */
    @Column
    private String clientSecret;

    /**
     * 授权码模式
     */
    @Column
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @Column
    private String redirectUri;

    /**
     * TOKEN失效时长（秒）
     */
    @Column
    private Double accessTokenValiditySeconds;

    /**
     * REFRESH_TOKEN失效时长（秒）
     */
    @Column
    private Double refreshTokenValiditySeconds;

    /**
     * 作用域
     */
    @Column
    private String scopes;

    /**
     * 资源id
     */
    @Column
    private String resourceId;
}