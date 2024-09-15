package com.tankbattle.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("/game").setAllowedOrigins("http://localhost:8080", "http://localhost:5173"); // client will send messages to this path
        config.addEndpoint("/game").setAllowedOrigins("http://localhost:8080", "http://localhost:5173").withSockJS(); // client will fallback to html REST, if websockets fail
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/syncfrom"); // server sends messages to clients subscribing messages on this path
        registry.setApplicationDestinationPrefixes("/syncto"); // server creates endpoints in this path for clients to send messages to
    }
}
