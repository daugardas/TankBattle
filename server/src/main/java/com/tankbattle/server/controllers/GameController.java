package com.tankbattle.server.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.models.Player;

@Controller
public class GameController {

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ArrayList<Player> players = new ArrayList();
    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

    public void addPlayer(Player player) {
        sessionIdToPlayerIndex.put(player.getSessionId(), this.players.size());
        this.players.add(player);
    }

    public void removePlayerBySessionId(String sessionId) {
        players.removeIf(p -> p.getSessionId().equals(sessionId));
        sessionIdToPlayerIndex.remove(sessionId);

        for (int i = 0; i < players.size(); i++) {
            sessionIdToPlayerIndex.put(players.get(i).getSessionId(), i);
        }
    }

    @Scheduled(fixedRate = 33)
    public void sendPlayers() {
        messagingTemplate.convertAndSend("/server/players", players);
    }

    @Scheduled(fixedRate = 33)
    public void update() {
        for (Player player : players) {
            player.updateLocation();
            System.out.println(player.toString());
        }
    }

    @MessageMapping("/update-player-movement")
    public void updatePlayer(@Payload byte movementDirection, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        int playerIndex = sessionIdToPlayerIndex.get(sessionId);

        players.get(playerIndex).setMovementDirection(movementDirection);
    }
}
