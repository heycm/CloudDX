package com.cloudx.platform.websocket.core.heartbeat;

import com.cloudx.common.tools.Jackson;
import com.cloudx.common.tools.threadpool.virtual.VirtualThread;
import com.cloudx.platform.websocket.autoconfigure.WebSocketProperties;
import com.cloudx.platform.websocket.constant.ServerConstant;
import com.cloudx.platform.websocket.core.session.SessionWrapper;
import com.cloudx.platform.websocket.core.session.WebSocketSessionLocalStorage;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 心跳监控
 * @author heycm
 * @version 1.0
 * @since 2025/4/30 23:05
 */
public class HeartbeatMonitor {

    private static final String SESSION_CLOSE_QUEUE = "websocket:session:close:queue";

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    private final WebSocketProperties.Heartbeat heartbeatConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    public HeartbeatMonitor(WebSocketProperties.Heartbeat heartbeat, RedisTemplate<String, Object> redisTemplate, SimpMessagingTemplate simpMessagingTemplate) {
        this.heartbeatConfig = heartbeat;
        this.redisTemplate = redisTemplate;
        this.messagingTemplate = simpMessagingTemplate;
        this.startMonitoring();
        this.startSessionCloseListener();
    }

    private void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkHeartbeats,
                heartbeatConfig.getCheckInterval(), heartbeatConfig.getCheckInterval(), TimeUnit.MILLISECONDS);
    }

    private void startSessionCloseListener() {
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, pattern) -> {
            String sessionId = new String(message.getBody());
            WebSocketSessionLocalStorage.close(sessionId);
        }, SESSION_CLOSE_QUEUE.getBytes());
    }

    private void checkHeartbeats() {
        long currentTime = System.currentTimeMillis();
        String sessionKeys = ServerConstant.SESSION_KEY_PREFIX + "*";
        int batchSize = 1000;

        // 使用scan命令分批次扫描key
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(KeyScanOptions.scanOptions().match(sessionKeys).count(batchSize).build())
        )) {
            List<byte[]> batchSessionKeys = new ArrayList<>(batchSize);
            while (cursor.hasNext()) {
                batchSessionKeys.add(cursor.next());
                if (batchSessionKeys.size() >= batchSize) {
                    processBatchHeartbeats(batchSessionKeys, currentTime);
                    batchSessionKeys.clear();
                }
            }
            if (!batchSessionKeys.isEmpty()) {
                processBatchHeartbeats(batchSessionKeys, currentTime);
            }
        }

    }

    private void processBatchHeartbeats(List<byte[]> batchSessionKeys, long currentTime) {
        List<Object> sessions = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (byte[] key : batchSessionKeys) {
                    connection.hashCommands().hGetAll(key);
                }
                return null;
            }
        });

        for (int i = 0; i < batchSessionKeys.size(); i++) {
            String sessionKey = redisTemplate.getStringSerializer().deserialize(batchSessionKeys.get(i));
            Object session = sessions.get(i);
            if (session == null) {
                Optional.ofNullable(sessionKey).map(key -> key.replace(ServerConstant.SESSION_KEY_PREFIX, ""))
                        .ifPresent(this::closeSession);
                continue;
            }
            SessionWrapper sessionWrapper = Jackson.toObject(Jackson.toJson(session), SessionWrapper.class);
            // 超过重试次数或超过最大心跳间隔，则关闭会话
            if (sessionWrapper.getPingAttempts() >= heartbeatConfig.getMaxRetries()
                    || sessionWrapper.getLastHeartbeatTime() <= currentTime - heartbeatConfig.getMaxInterval()) {
                closeSession(sessionWrapper.getSessionId());
            }
            // 达到检查间隔时发送心跳
            else if (sessionWrapper.getLastHeartbeatTime() <= currentTime - heartbeatConfig.getCheckInterval()) {
                pingSession(sessionWrapper.getSessionId());
            }
        }
    }

    private void closeSession(final String sessionId) {
        VirtualThread.execute(() -> {
            boolean closed = WebSocketSessionLocalStorage.close(sessionId);
            if (!closed) {
                redisTemplate.convertAndSend(SESSION_CLOSE_QUEUE, sessionId);
            }
        });
    }

    private void pingSession(final String sessionId) {
        VirtualThread.execute(() -> {
            messagingTemplate.convertAndSendToUser(sessionId, ServerConstant.CLIENT_HEARTBEAT_DESTINATION, "ping");
        });
    }
}
