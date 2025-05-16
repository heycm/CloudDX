package com.cloudx.im.test;

import java.security.Principal;

import com.cloudx.platform.websocket.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * @author heycm
 * @version 1.0
 * @since 2025/5/15 23:24
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class TestController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessagingService messagingService;

    @MessageMapping("/test")
    public void test(String playload, SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        System.out.println("sessionId: " + sessionId);
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/test", playload);
        messagingTemplate.convertAndSend("/topic/test", playload);
    }

    @MessageMapping("/test2")
    public void test2(String playload, Principal principal) {
        String name = principal.getName();
        messagingTemplate.convertAndSendToUser(name, "/queue/test2", playload);
    }

    @MessageMapping("/test3")
    @SendToUser(value = "/queue/test3", broadcast = false)
    public String test3(String playload) {
        return playload + " from test3";
    }
}
