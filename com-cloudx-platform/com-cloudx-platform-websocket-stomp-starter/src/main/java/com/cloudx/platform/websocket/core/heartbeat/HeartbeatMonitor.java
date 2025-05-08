package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.core.repository.SessionRepository;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import com.cloudx.platform.websocket.core.session.WebSocketSessionLocalStorage;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 心跳监控
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 23:05
 */
public class HeartbeatMonitor {

    private static final String SESSION_CLOSE_QUEUE = "websocket:session:close:queue";

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final SessionRepository sessionRepository;

    private final WebSocketProperties.Heartbeat heartbeatConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    public HeartbeatMonitor(SessionRepository sessionRepository, WebSocketProperties.Heartbeat heartbeat, RedisTemplate<String, Object> redisTemplate, SimpMessagingTemplate simpMessagingTemplate) {
        this.sessionRepository = sessionRepository;
        this.heartbeatConfig = heartbeat;
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = simpMessagingTemplate;
        this.startMonitoring();
        this.startSessionCloseListener();
    }

    private void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkHeartbeats, heartbeatConfig.getCheckInterval(), heartbeatConfig.getCheckInterval(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    private void checkHeartbeats() {
        long currentTime = System.currentTimeMillis();
        String sessionKeys = "websocket:session:*";
        int batchSize = 1000;
        byte[] heartbeatField = redisTemplate.getStringSerializer().serialize("lastHeartbeatTime");


        // 使用scan命令分批次扫描key
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(KeyScanOptions.scanOptions().match(sessionKeys).count(batchSize).build())
        )) {
            List<byte[]> batchSessionKeys = new ArrayList<>(batchSize);
            while (cursor.hasNext()) {
                batchSessionKeys.add(cursor.next());
                if (batchSessionKeys.size() >= batchSize) {
                    processBatchHeartbeats(batchSessionKeys, heartbeatField, currentTime);
                    batchSessionKeys.clear();
                }
            }
            if (!batchSessionKeys.isEmpty()) {
                processBatchHeartbeats(batchSessionKeys, heartbeatField, currentTime);
            }
        }

    }

    private void processBatchHeartbeats(List<byte[]> batchSessionKeys, byte[] heartbeatField, long currentTime) {
        List<Object> sessions = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (byte[] key : batchSessionKeys) {
                    connection.hashCommands().hGetAll(key);
                }
                return null;
            }
        });

        List<SessionWrapper> pingList = new ArrayList<>(batchSessionKeys.size());
        List<SessionWrapper> closeList = new ArrayList<>(batchSessionKeys.size());


        List<String> pingKeys = new ArrayList<>(batchSessionKeys.size());
        List<String> closeKeys = new ArrayList<>(batchSessionKeys.size());
        for (int i = 0; i < batchSessionKeys.size(); i++) {
            String key = redisTemplate.getStringSerializer().deserialize(batchSessionKeys.get(i));
            Object heartbeatTimeObj = heartbeatTimes.get(i);
            if (heartbeatTimeObj == null) {
                closeKeys.add(key);
                continue;
            }
            try {
                long heartbeatTime = Long.parseLong(new String((byte[]) heartbeatTimeObj, StandardCharsets.UTF_8));
                if (currentTime - heartbeatTime >= heartbeatConfig.getMaxInterval()) {
                    closeKeys.add(key);
                }
                if (currentTime - heartbeatTime >= heartbeatConfig.getCheckInterval() / 2) {
                    pingKeys.add(key);
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    @MessageMapping("/app/ping")
    @SendToUser("/queue/pong")
    public String ping(SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        SessionWrapper session = sessionRepository.getSession(sessionId);
        if (session != null) {
            session.updateHeartbeat();
            sessionRepository.save(session);
        }
        return "pong";
    }

    private void handleExpiredSession(SessionWrapper session, long currentTime) {
        if (session.getPingAttempts() >= heartbeatConfig.getMaxRetries() || currentTime - session.getLastHeartbeatTime() >= heartbeatConfig.getMaxInterval()) {
            closeSession(session);
        } else {
            pingSession(session);
            session.incrementPingAttempt();
            sessionRepository.save(session);
        }
    }

    private void closeSession(SessionWrapper session) {
        boolean closed = WebSocketSessionLocalStorage.close(session.getSessionId());
        if (!closed) {
            redisTemplate.convertAndSend(SESSION_CLOSE_QUEUE, session.getSessionId());
        }
    }

    private void pingSession(SessionWrapper session) {
        messagingTemplate.convertAndSendToUser(session.getUserId(), "/user/ping", "ping");
    }

    private void startSessionCloseListener() {
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            String sessionId = new String(message.getBody());
            WebSocketSessionLocalStorage.close(sessionId);
        }, SESSION_CLOSE_QUEUE.getBytes());
    }
}
