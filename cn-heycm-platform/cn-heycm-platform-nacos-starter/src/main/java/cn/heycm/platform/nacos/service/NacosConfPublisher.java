package cn.heycm.platform.nacos.service;

/**
 * 推送配置
 * @author heycm
 * @version 1.0
 * @since 2025/3/25 21:27
 */
public interface NacosConfPublisher {

    /**
     * 推送配置到默认分组 DEFAULT_GROUP 配置ID为 dataId 的配置文件
     * @param dataId  配置ID
     * @param content 配置内容
     * @return true 成功
     */
    boolean publish(String dataId, String content);

    /**
     * 推送配置到指定分组 group 配置ID为 dataId 的配置文件
     * @param dataId  配置ID
     * @param group   配置分组
     * @param content 配置内容
     * @return true 成功
     */
    boolean publish(String dataId, String group, String content);

}
