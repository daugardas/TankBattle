package com.tankbattle;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.models.Player;

public class GameSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/server/players", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Player[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                Player[] players = (Player[]) o;

                GameManager.getInstance().addPlayers(players);

            }

        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
            Throwable exception) {
        System.out.println(exception);
    }
}
