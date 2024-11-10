package com.tankbattle.controllers;

import com.tankbattle.models.Bullet;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.utils.ServerFPSCounter;
import com.tankbattle.utils.Vector2;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameSessionHandler extends StompSessionHandlerAdapter {
    public StompSession stompSession;

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        // System.out.println("transport error: " + exception);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        stompSession = session;

        ServerFPSCounter.getInstance().start();

        session.subscribe("/server/players", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Player[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                List<Player> temp = new ArrayList<>(List.of((Player[]) o));
                GameManager.getInstance().addPlayers(new ArrayList<>(List.of((Player[]) o)));
                ServerFPSCounter.getInstance().incrementServerUpdateCount();
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

        session.subscribe("/server/bullets", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Bullet[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ArrayList<Bullet> bullets = new ArrayList<>(List.of((Bullet[]) o));
                if (!bullets.isEmpty()) {
                    GameManager.getInstance().updateBullets(bullets);
                } else {
                    GameManager.getInstance().clearBullets();
                }

            }
        });

        session.subscribe("/server/collisions", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Vector2.class; // Expecting a Map with "x" and "y"
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object payload) {
                CollisionManager.getInstance().addCollision((Vector2) payload);
            }
        });
    }


    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        // System.out.println(exception);
    }
}
