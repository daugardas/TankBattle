package com.tankbattle.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.tankbattle.server.components.WebSocketSessionManager;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private WebSocketSessionManager sessionManager;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/server"); // server sends messages to clients subscribing messages on this path
        registry.setApplicationDestinationPrefixes("/client"); // server creates endpoints in this path for clients to
                                                               // send messages to
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry config) {
        config.addEndpoint("/game").setAllowedOriginPatterns("*"); // client will send messages to this path
        config.addEndpoint("/game").setAllowedOriginPatterns("*").withSockJS(); // client will fallback to html REST, if
                                                                                // websockets fail
    }

    @Override
    public void configureWebSocketTransport(@NonNull WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final @NonNull WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final @NonNull WebSocketSession session) throws Exception {
                        String id = session.getId();
                        System.out.println("Connection established with '" + id + "'");
                        sessionManager.addSession(session);

                        super.afterConnectionEstablished(session);
                    }
                };
            }
        });
    }
}
