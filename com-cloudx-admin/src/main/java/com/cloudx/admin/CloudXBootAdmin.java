package com.cloudx.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CloudX Boot Admin Server
 * @author heycm
 * @version 1.0
 * @since 2025/6/7 20:27
 */
@SpringBootApplication
@EnableAdminServer
public class CloudXBootAdmin {

    public static void main(String[] args) {
        SpringApplication.run(CloudXBootAdmin.class, args);
    }

}
