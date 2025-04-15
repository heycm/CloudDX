package com.cloudx.platform.apisign;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.apisign.filter.SignFilter;
import com.cloudx.platform.apisign.properties.SignProperties;
import com.cloudx.platform.apisign.repository.NonceRepository;
import com.cloudx.platform.apisign.repository.SignRepository;
import com.cloudx.platform.apisign.repository.impl.NonceRepositoryImpl;
import com.cloudx.platform.apisign.repository.impl.SignRepositoryImpl;
import com.cloudx.platform.nacos.service.NacosConfListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Slf4j
public class ApiSignAutoConfiguration {

    private static final String DATA_ID = "micro-apisign.json";

    public ApiSignAutoConfiguration() {
        log.info("platform component [ApiSign] starter ready...");
    }

    @Bean
    public SignRepository signRepository(NacosConfListener nacosConfListener) {
        SignRepository repository = new SignRepositoryImpl();
        nacosConfListener.addListener(DATA_ID, confs -> {
            List<SignProperties> properties = Jackson.toList(confs, SignProperties.class);
            properties.forEach(repository::saveProperties);
        });
        return repository;
    }

    @Bean
    public NonceRepository nonceRepository(RedisTemplate<String, Object> redisTemplate) {
        return new NonceRepositoryImpl(redisTemplate);
    }

    @Bean
    public SignFilter signFilter(SignRepository signRepository, NonceRepository nonceRepository) {
        return new SignFilter(signRepository, nonceRepository);
    }
}
