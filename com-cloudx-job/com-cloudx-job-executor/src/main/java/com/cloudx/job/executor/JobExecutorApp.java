package com.cloudx.job.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Job执行器启动类
 * @author heycm
 * @version 1.0
 * @since 2025/6/10 22:50
 */
@SpringBootApplication
public class JobExecutorApp {

    public static void main(String[] args) {
        SpringApplication.run(JobExecutorApp.class, args);
    }

}
