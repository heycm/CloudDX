package com.cloudx.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IM APP
 * @author heycm
 * @version 1.0
 * @since 2025/5/14 22:07
 */
@SpringBootApplication
@RestController
public class IMApp {

    public static void main(String[] args) {
        SpringApplication.run(IMApp.class, args);
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
