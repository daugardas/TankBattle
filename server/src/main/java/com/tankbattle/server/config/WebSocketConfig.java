package com.tankbattle.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/server"); // server sends messages to clients subscribing messages on this path
        registry.setApplicationDestinationPrefixes("/client"); // server creates endpoints in this path for clients to send messages to
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("/game").setAllowedOriginPatterns("*"); // client will send messages to this path
        config.addEndpoint("/game").setAllowedOriginPatterns("*").withSockJS(); // client will fallback to html REST, if websockets fail
    }
}
