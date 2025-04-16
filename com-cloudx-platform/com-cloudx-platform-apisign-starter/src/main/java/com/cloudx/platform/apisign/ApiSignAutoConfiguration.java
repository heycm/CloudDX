package com.cloudx.platform.apisign;

import com.cloudx.common.tools.Jackson;
import com.cloudx.platform.apisign.filter.SignFilter;
import com.cloudx.platform.apisign.properties.SignProperties;
import com.cloudx.platform.apisign.repository.NonceRepository;
import com.cloudx.platform.apisign.repository.SignRepository;
import com.cloudx.platform.apisign.repository.PathRepository;
import com.cloudx.platform.apisign.repository.impl.NonceRepositoryImpl;
import com.cloudx.platform.apisign.repository.impl.SignRepositoryImpl;
import com.cloudx.platform.apisign.repository.impl.PathRepositoryImpl;
import com.cloudx.platform.nacos.service.NacosConfListener;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Slf4j
public class ApiSignAutoConfiguration {

    private static final String GROUP = "API_SIGN";
    private static final String DATA_ID = "apisign.json";
    private static final String URL_DATA_ID = "apisign-exclude-url.json";

    private final NacosConfListener nacosConfListener;

    public ApiSignAutoConfiguration(NacosConfListener nacosConfListener) {
        this.nacosConfListener = nacosConfListener;
        log.info("platform component [ApiSign] starter ready...");
    }

    @Bean
    public SignRepository signRepository() {
        SignRepository repository = new SignRepositoryImpl();
        nacosConfListener.addListener(DATA_ID, GROUP, confs -> {
            List<SignProperties> properties = Jackson.toList(confs, SignProperties.class);
            repository.saveProperties(properties);
        });
        return repository;
    }

    @Bean
    public NonceRepository nonceRepository(RedisTemplate<String, Object> redisTemplate) {
        return new NonceRepositoryImpl(redisTemplate);
    }

    @Bean
    public PathRepository pathRepository() {
        PathRepositoryImpl repository = new PathRepositoryImpl();
        nacosConfListener.addListener(URL_DATA_ID, GROUP, confs -> {
            Set<String> paths = Jackson.toSet(confs, String.class);
            repository.save(paths);
        });
        return repository;
    }

    @Bean
    public SignFilter signFilter(SignRepository signRepository, NonceRepository nonceRepository,
                                 PathRepository pathRepository) {
        return new SignFilter(signRepository, nonceRepository, pathRepository);
    }
}
