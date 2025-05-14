package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;

/**
 * 心跳接口
 * @author heycm
 * @version 1.0
 * @since 2025/5/14 17:51
 */
// @Controller
public class HeartbeatController {

    private final SessionRepository sessionRepository;

    public HeartbeatController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * 客户端心跳接口
     * @param accessor
     * @return pong
     */
    @MessageMapping(ServerConstant.SERVER_HEARTBEAT_DESTINATION)
    @SendToUser(ServerConstant.CLIENT_HEARTBEAT_DESTINATION)
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        SessionWrapper session = sessionRepository.getSession(sessionId);
        if (session != null) {
            session.updateHeartbeat();
            sessionRepository.save(session);
        }
        return "pong";
    }

}
