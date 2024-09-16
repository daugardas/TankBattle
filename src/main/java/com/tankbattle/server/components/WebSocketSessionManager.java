package com.tankbattle.server.components;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {
    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, String username){
        activeSessions.put(sessionId, username);
    }

    public void removeSession(String sessionId){
        activeSessions.remove(sessionId);
    }

    public boolean isSessionActive(String sessionId){
        return activeSessions.containsKey(sessionId);
    }

    public String getUsername(String sessionId){
        return activeSessions.get(sessionId);
    }
}
