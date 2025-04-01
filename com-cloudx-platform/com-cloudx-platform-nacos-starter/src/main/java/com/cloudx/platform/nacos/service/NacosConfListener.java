package com.cloudx.platform.nacos.service;

import java.util.function.Consumer;

/**
 * 配置监听
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:30
 */
public interface NacosConfListener {

    /**
     * 注册监听器，监听默认分组 DEFAULT_GROUP 配置ID为 dataId 的配置文件
     * @param dataId   配置ID
     * @param receiver 配置更新消费
     */
    void addListener(String dataId, Consumer<String> receiver);

    /**
     * 注册监听器，定分组 group 配置ID为 dataId 的配置文件
     * @param dataId   配置ID
     * @param group    配置分组
     * @param receiver 配置更新消费
     */
    void addListener(String dataId, String group, Consumer<String> receiver);
}
