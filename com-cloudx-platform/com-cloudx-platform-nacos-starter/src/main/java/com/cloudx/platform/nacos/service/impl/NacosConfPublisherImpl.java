package com.cloudx.platform.nacos.service.impl;

import com.cloudx.platform.nacos.service.NacosConfPublisher;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

/**
 * 推送配置实现
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:33
 */
@Slf4j
public class NacosConfPublisherImpl implements NacosConfPublisher {


    private final ConfigService configService;

    public NacosConfPublisherImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean publish(String dataId, String content) {
        return this.publish(dataId, "DEFAULT_GROUP", content);
    }

    @Override
    public boolean publish(String dataId, String group, String content) {
        try {
            log.info("推送[dataId: {}, group: {}]配置更新: {}", dataId, group, content);
            return configService.publishConfig(dataId, group, content);
        } catch (NacosException e) {
            log.error("推送配置发生异常", e);
        }
        return false;
    }
}
