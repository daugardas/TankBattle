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
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/for-all-clients", "/for-specific-client"); // server sends messages to clients subscribing messages on this path
        registry.setApplicationDestinationPrefixes("/from-client"); // server creates endpoints in this path for clients to send messages to
        registry.setUserDestinationPrefix("/user"); // clients can listen to endpoints here meant only for them
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("/game").setAllowedOriginPatterns("*").setHandshakeHandler(new UserHandshakeHandler()); // client will send messages to this path
        config.addEndpoint("/game").setAllowedOriginPatterns("*").setHandshakeHandler(new UserHandshakeHandler()).withSockJS(); // client will fallback to html REST, if websockets fail


    }
}
