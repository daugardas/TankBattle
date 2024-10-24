package com.tankbattle.controllers;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.tankbattle.models.Level;
import com.tankbattle.utils.Vector2;

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
                GameManager.getInstance().incrementServerUpdateCount();
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

        session.subscribe("/server/collisions", new StompFrameHandler() {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Map.class; // Expecting a Map with "x" and "y"
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object payload) {
            if (payload instanceof Map) {
                Map<String, Object> collisionData = (Map<String, Object>) payload;
                Integer x = (Integer) collisionData.get("x");
                Integer y = (Integer) collisionData.get("y");
                System.out.println("Collision detected at location: x=" + x + ", y=" + y);
                Vector2 collisionLocation = new Vector2(x, y);
                GameManager.getInstance().addCollision(collisionLocation);
            } else {
                System.out.println("Received unexpected collision data: " + payload);
            }
        }
    });
}



    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
        System.out.println(exception);
    }
}
