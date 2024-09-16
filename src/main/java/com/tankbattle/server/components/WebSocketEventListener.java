package com.tankbattle.server.components;

import com.tankbattle.server.controllers.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private GameController gameController;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getUser().getName();

        sessionManager.addSession(sessionId, username);

        System.out.println("WebSocket connection found. Session ID: " + sessionId + ", user: " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getUser().getName();

        sessionManager.removeSession(sessionId);

        System.out.println("WebSocket connection lost. Session ID: " + sessionId + ", user: " + username);

        gameController.removePlayerBySessionId(sessionId);
    }
}
