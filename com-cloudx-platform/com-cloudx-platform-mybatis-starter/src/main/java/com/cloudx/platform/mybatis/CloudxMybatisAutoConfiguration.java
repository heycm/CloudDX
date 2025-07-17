package com.cloudx.platform.mybatis;

import com.cloudx.platform.mybatis.batch.MyBatisBatchHelper;
import com.cloudx.platform.mybatis.plugins.SQLMarkingInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * MyBatis自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:53
 */
@Configuration
@MapperScan(basePackages = {"com.cloudx.*.infrastructure.db.mapper"})
@Slf4j
public class CloudxMybatisAutoConfiguration {

    public CloudxMybatisAutoConfiguration() {
        log.info("platform component [Mybatis] starter ready...");
    }

    @Bean
    public SQLMarkingInterceptor sqlMarkingInterceptor() {
        return new SQLMarkingInterceptor();
    }

    @Bean
    @DependsOn("sqlSessionFactory")
    public MyBatisBatchHelper myBatisBatchHelper(SqlSessionFactory sqlSessionFactory) {
        return new MyBatisBatchHelper(sqlSessionFactory);
    }
}
