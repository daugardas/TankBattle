package com.tankbattle.controllers;

import com.tankbattle.commands.ICommand;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class WebSocketManager {

    private final WebSocketClient webSocketClient;
    private final WebSocketStompClient stompClient;
    private final GameSessionHandler sessionHandler;

    public WebSocketManager() {
        webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        sessionHandler = new GameSessionHandler();
    }

    public boolean connect(String hostname, String username) {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", username);

        String url = String.format("ws://%s:8080/game", hostname);
        System.out.println("Connecting to: " + url);
        try {
            CompletableFuture<StompSession> sessionFuture = stompClient.connectAsync(url, (WebSocketHttpHeaders) null, connectHeaders, sessionHandler);
            StompSession session = sessionFuture.join();
            System.out.println("Session connected: " + session.isConnected());
            return session.isConnected();
        } catch (CompletionException e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public void sendCommand(ICommand command) {

        // System.out.println("session is open: " +
        // sessionHandler.stompSession.isConnected());

        sessionHandler.stompSession.send("/client/command", command);
    }

    public void close() {
        sessionHandler.stompSession.disconnect();
    }
}
