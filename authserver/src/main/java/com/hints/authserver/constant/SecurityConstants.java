package com.hints.authserver.constant;

import java.util.concurrent.TimeUnit;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    /**
     * client缓存有效时间单位
     */
    public static final TimeUnit CLIENT_CACHE_TIMEOUT_UNIT = TimeUnit.MINUTES;

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 超级管理员密码
     */
    public static final String ADMINISTRATORS_SECRET = "g2ee_pk23fuck";
}
