package cn.heycm.platform.mybatis;

import cn.heycm.platform.mybatis.batch.MyBatisBatchHelper;
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
@MapperScan(basePackages = {"cn.heycm.*.infrastructure.db.mapper"})
@Slf4j
public class MybatisAutoConfiguration {

    public MybatisAutoConfiguration() {
        log.info("platform component [Mybatis] starter ready...");
    }

    @Bean
    public MyBatisBatchHelper myBatisBatchHelper(SqlSessionFactory sqlSessionFactory) {
        return new MyBatisBatchHelper(sqlSessionFactory);
    }

}
