package com.tankbattle.controllers;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.tankbattle.GameSessionHandler;

public class WebSocketManager {

    private WebSocketClient webSocketClient;
    private WebSocketStompClient stompClient;
    private GameSessionHandler sessionHandler;

    public WebSocketManager() {
        webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        sessionHandler = new GameSessionHandler();
    }

    public void connect(String hostname, String username) {
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("login", username);

        String url = String.format("ws://%s:8080/game", hostname);
        stompClient.connectAsync(url, (WebSocketHttpHeaders) null, connectHeaders, sessionHandler);
    }

    public void sendMovementBuffer(int[] movementBuffer) {
        sessionHandler.stompSession.send("/client/update-player-movement", movementBuffer);
    }
}
