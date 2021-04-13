package com.hints.authserver.config;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;

public class CustomJdbcAuthorizationCodeServices extends JdbcAuthorizationCodeServices {
    private RandomValueStringGenerator generator = new RandomValueStringGenerator(32);

    public CustomJdbcAuthorizationCodeServices(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = generator.generate();
        store(code, authentication);
        return code;
    }
}
