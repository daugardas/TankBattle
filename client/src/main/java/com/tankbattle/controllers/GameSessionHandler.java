package com.tankbattle.controllers;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.tankbattle.models.Level;

public class GameSessionHandler extends StompSessionHandlerAdapter {
    public StompSession stompSession;

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("transport error: " + exception);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        stompSession = session;

        session.subscribe("/server/players", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                GameManager.getInstance().addPlayers((Object[]) o);
            }
        });

        session.subscribe("/client/level", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Level.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                GameManager.getInstance().setLevel((Level) o);
            }

        });

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
        System.out.println(exception);
    }
}
