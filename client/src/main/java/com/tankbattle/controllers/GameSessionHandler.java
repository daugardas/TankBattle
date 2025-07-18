package com.tankbattle.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.tankbattle.mediator.ScoreMediatorImpl;
import com.tankbattle.models.Bullet;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.models.PowerUp;
import com.tankbattle.utils.ServerFPSCounter;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameSessionHandler extends StompSessionHandlerAdapter {
    public StompSession stompSession;

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Session: " + session);
        System.out.println("transport error: " + exception);
        GameWindow.getInstance().resetToMenu();
        GameManager.getInstance().stopGame();
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
                List<Player> players = new ArrayList<>(List.of((Player[]) o));
                GameManager.getInstance().addPlayers(players);
                ScoreMediatorImpl.getInstance().updateScores(players);
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
                System.out.println("Level received");
                GameManager.getInstance().setLevel((Level) o);
            }

        });

        session.subscribe("/server/level", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Level.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                System.out.println("Level received");
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

        session.subscribe("/server/powerups", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return PowerUp[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ArrayList<PowerUp> powerUps = new ArrayList<>(List.of((PowerUp[]) o));
                GameManager.getInstance().updatePowerUps(powerUps);
            }
        });

        session.subscribe("/server/tank-destroyed", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Vector2.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                Vector2 location = (Vector2) o;
                CollisionManager.getInstance().addExplosion(location);
            }
        });
    }


    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Session: " + session);
        System.out.println(exception);
    }
}
