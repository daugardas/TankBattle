package com.tankbattle.server.controllers;

import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.models.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;

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
        System.err.println(this.players);
        this.players.removeIf(p -> p.getSessionId().equals(sessionId));
        sessionIdToPlayerIndex.remove(sessionId);
    }

    @MessageMapping("/update-player")
    public void updatePlayer(@Payload Player player, SimpMessageHeaderAccessor headerAccessor) {
        int playerIndex = this.players.indexOf(player);
        this.players.get(playerIndex).setLocation(player.getLocation());
    }

    @Scheduled(fixedRate = 33)
    public void sendPlayers() {
        messagingTemplate.convertAndSend("/server/players", this.players);
    }

    @Scheduled(fixedRate = 33)
    public void update()
    {
        for (Player player : players) {
            player.updateLocation();
        }
    }

    @MessageMapping("/update-player-movement")
    public void updatePlayer(@Payload byte movementDirection, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        int playerIndex = sessionIdToPlayerIndex.get(sessionId);

        this.players.get(playerIndex).setMovementDirection(movementDirection);
        System.out.println(movementDirection);
    }
}
