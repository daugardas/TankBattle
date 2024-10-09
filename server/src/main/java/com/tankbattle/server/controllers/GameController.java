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

import com.tankbattle.server.builders.LevelBuilder;
import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.factories.ProceduralGeneratorFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.strategies.Level.ProceduralGenerator;

import jakarta.annotation.PostConstruct;

@Controller
public class GameController {

    public static final int WORLD_HEIGHT = 20;
    public static final int WORLD_WIDTH = 20;

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ArrayList<Player> players = new ArrayList();
    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

    public Level level;

    @PostConstruct
    public void init() {
        ProceduralGenerator generator = ProceduralGeneratorFactory.createGenerator("random");

        LevelBuilder levelBuilder = new LevelBuilder(generator);
        level = levelBuilder.buildLevel(WORLD_WIDTH, WORLD_HEIGHT);

        System.out.println("Level initialized. Level:");
        System.out.println(level.toString());
    }

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
            //System.out.println(player.toString());
        }
    }

    @MessageMapping("/update-player-movement")
    public void updatePlayer(@Payload byte movementDirection, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        int playerIndex = sessionIdToPlayerIndex.get(sessionId);

        players.get(playerIndex).setMovementDirection(movementDirection);
    }
}
