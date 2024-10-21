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

    //
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getFirstNativeHeader("login");

        sessionManager.addSession(sessionId);

        int x = 2000; //needs fixing Daugardas plz why is it clashing between world coordinates and tiles
        int y = 3000; 

        Player newPlayer = new Player(sessionId, username, x, y);
        gameController.addPlayer(newPlayer);

        System.out.println("WebSocket connection found. Session ID: " + sessionId + ", user: " + username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        sessionManager.removeSession(sessionId);
        gameController.removePlayerBySessionId(sessionId);
    }
}
