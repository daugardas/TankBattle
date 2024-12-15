package com.tankbattle.server.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.Player;

@Component
public class WebSocketEventListener {
    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private GameController gameController;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getFirstNativeHeader("login");

        sessionManager.addSessionId(sessionId);

        // Create temporary player - actual spawn location will be set in GameController.addPlayer
        Player newPlayer = new Player();
        newPlayer.setSessionId(sessionId);
        newPlayer.setUsername(username);
        gameController.addPlayer(newPlayer);

        System.out.println("WebSocket connection found. Session ID: " + sessionId + ", user: " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        sessionManager.removeSession(sessionId);
        gameController.removePlayerBySessionId(sessionId);
    }
}
