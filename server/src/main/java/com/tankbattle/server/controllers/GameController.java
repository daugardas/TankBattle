package com.tankbattle.server.controllers;

import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class GameController {

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ArrayList<Player> players = new ArrayList();
    private ConcurrentHashMap<String, String> sessionIdToPlayerUuid = new ConcurrentHashMap<>();

    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    public void removePlayerBySessionId(String sessionId){
        System.err.println(this.players);
        this.players.removeIf(p -> p.getSessionId().equals(sessionId));
        sessionIdToPlayerUuid.remove(sessionId);
    }

    @MessageMapping("/update-player")
    public void updatePlayer(@Payload Player player) {
        int playerIndex = this.players.indexOf(player);
        this.players.get(playerIndex).setLocation(player.getLocation());
    }

    @Scheduled(fixedRate = 33)
    public void sendPlayers(){
        messagingTemplate.convertAndSend("/server/players", this.players);
    }

}
