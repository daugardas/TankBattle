package com.tankbattle.server.components;

import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSessionManager {
    private final ArrayList<String> activeSessions = new ArrayList<>();

    public void addSession(String sessionId){
        activeSessions.add(sessionId);
    }

    public void removeSession(String sessionId){
        activeSessions.remove(sessionId);
    }

    public boolean isSessionActive(String sessionId){
        return activeSessions.contains(sessionId);
    }
}
