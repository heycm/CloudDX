package com.cloudx.platform.mybatis;

import com.cloudx.platform.mybatis.batch.MyBatisBatchHelper;
import com.cloudx.platform.mybatis.plugins.SQLMarkingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:53
 */
@Configuration
@MapperScan(basePackages = {"com.cloudx.*.infrastructure.db.mapper"})
@Slf4j
public class MybatisAutoConfiguration {

    public MybatisAutoConfiguration(SqlSessionFactory sqlSessionFactory) {
        log.info("platform component [Mybatis] starter ready...");
        MyBatisBatchHelper.setSqlSessionFactory(sqlSessionFactory);
    }

    @Bean
    public SQLMarkingInterceptor sqlMarkingInterceptor() {
        return new SQLMarkingInterceptor();
    }
}
