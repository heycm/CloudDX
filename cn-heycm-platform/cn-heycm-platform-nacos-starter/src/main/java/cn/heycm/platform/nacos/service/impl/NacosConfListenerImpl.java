package cn.heycm.platform.nacos.service.impl;

import cn.heycm.platform.nacos.service.NacosConfListener;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置监听实现
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:32
 */
@Slf4j
public class NacosConfListenerImpl implements NacosConfListener {

    private final ConfigService configService;

    public NacosConfListenerImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void addListener(String dataId, Consumer<String> receiver) {
        this.addListener(dataId, "DEFAULT_GROUP", receiver);
    }

    @Override
    public void addListener(String dataId, String group, Consumer<String> receiver) {
        log.info("注册[dataId: {}, group: {}]配置监听", dataId, group);
        try {
            String config = configService.getConfigAndSignListener(dataId, group, 3000L, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String config) {
                    log.info("收到[dataId: {}, group: {}]配置更新: {}", dataId, group, config);
                    receiver.accept(config);
                }
            });
            // boot init receive
            log.info("收到[dataId: {}, group: {}]配置更新: {}", dataId, group, config);
            receiver.accept(config);
        } catch (NacosException e) {
            log.info("注册监听器发生异常", e);
        }
    }
}
