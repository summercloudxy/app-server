package com.zgiot.app.server;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class AppServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppServerApplication.class, args);
    }
}
