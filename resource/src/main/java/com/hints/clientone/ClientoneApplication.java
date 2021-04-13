package com.hints.clientone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class ClientoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientoneApplication.class, args);
    }

}
