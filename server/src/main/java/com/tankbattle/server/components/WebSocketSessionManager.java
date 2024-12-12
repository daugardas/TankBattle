package com.tankbattle.server.components;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {
    private final CopyOnWriteArrayList<String> activeSessionIds = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<WebSocketSession> activeSessions = new CopyOnWriteArrayList<>();

    public void addSession(WebSocketSession session) {
        activeSessions.add(session);
    }

    public void addSessionId(String sessionId){
        activeSessionIds.add(sessionId);
    }

    public void removeSession(String sessionId){
        for (WebSocketSession session : activeSessions) {
            if(session.getId().equals(sessionId)){
                try {
                    session.close(CloseStatus.NORMAL);
                    activeSessionIds.remove(sessionId);
                    activeSessions.remove(session);
                } catch (IOException e) {
                    System.out.println("IOException thrown then trying to close session");
                }
            }
        }
    }

    public boolean isSessionActive(String sessionId){
        return activeSessionIds.contains(sessionId);
    }
}
