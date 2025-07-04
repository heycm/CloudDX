package com.cloudx.platform.domain;

import com.cloudx.platform.domain.command.AppCommandBusImpl;
import com.cloudx.platform.domain.command.CommandBus;
import com.cloudx.platform.domain.query.AppQueryBusImpl;
import com.cloudx.platform.domain.query.QueryBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DomainAutoConfiguration
 * @author heycm
 * @version 1.0
 * @since 2025/7/4 22:15
 */
@Configuration
@Slf4j
public class DomainAutoConfiguration {

    public DomainAutoConfiguration() {
        log.info("platform component [Domain] starter ready...");
    }

    @Bean
    public CommandBus commandBus(ApplicationContext applicationContext) {
        return new AppCommandBusImpl(applicationContext);
    }

    @Bean
    public QueryBus queryBus(ApplicationContext applicationContext) {
        return new AppQueryBusImpl(applicationContext);
    }
}
