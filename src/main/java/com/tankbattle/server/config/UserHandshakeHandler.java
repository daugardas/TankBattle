package com.tankbattle.server.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

class StompPrincipal implements Principal {
    private String name;

    public StompPrincipal(String name){
        this.name = name;
    }

    @Override
    public String getName(){
        return this.name;
    }
}

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes){
        return new StompPrincipal(UUID.randomUUID().toString());
    }
}
