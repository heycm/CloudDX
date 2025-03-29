package cn.heycm.platform.mq.common.event;

/**
 * 事件定义
 * @author heycm
 * @version 1.0
 * @since 2025/3/29 23:36
 */
public interface EventDef {

    /**
     * 事件主题
     * @return
     */
    String getTopic();

    /**
     * 事件名称
     * @return
     */
    String getEvent();

}
