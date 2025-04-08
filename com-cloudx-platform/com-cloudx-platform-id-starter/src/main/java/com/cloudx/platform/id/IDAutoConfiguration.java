package com.cloudx.platform.id;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import com.cloudx.common.entity.error.Assert;
import com.cloudx.platform.id.service.BaiduUidService;
import com.cloudx.platform.id.service.IdService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式ID配置
 */
@Configuration
@ConditionalOnProperty(value = "id.worker-id")
public class IDAutoConfiguration {

    @Value("${id.worker-id}")
    private int workId;

    @Bean
    public UidGenerator uidGenerator() {
        Assert.isTrue(workId > 0, "Invalid unique worker id: " + workId);
        WorkerIdAssigner workerIdAssigner = new DisposableWorkerIdAssigner(workId);
        return new CachedUidGenerator(workerIdAssigner);
    }

    @Bean
    @ConditionalOnBean(UidGenerator.class)
    public IdService idService(UidGenerator uidGenerator) {
        return new BaiduUidService(uidGenerator);
    }
}
