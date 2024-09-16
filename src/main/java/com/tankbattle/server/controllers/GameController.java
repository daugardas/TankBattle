package com.tankbattle.server.controllers;

import com.tankbattle.server.models.Player;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ArrayList<Player> players = new ArrayList();
    private ConcurrentHashMap<String, String> sessionIdToPlayerUuid = new ConcurrentHashMap<>();

    @MessageMapping("/disconnect")
    public void disconnectPlayer(@Payload Player player, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        this.players.removeIf(p -> p.equals(player));
        sessionIdToPlayerUuid.remove(headerAccessor.getSessionId());
    }

    @SubscribeMapping("/session-id")
    public String handleSessionIdSubscription(SimpMessageHeaderAccessor headerAccessor) {
        return headerAccessor.getSessionId();
    }

    @SubscribeMapping("/for-all-clients/players")
    public ArrayList<Player> getPlayers() {
        System.out.println("client subscribed to /players");
        return players;
    }

    @MessageMapping("/create-new-player")
    // /from-client/create-new-player
    public void createNewPlayer(@Payload Player player, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Client wants to create new player: " + player.toString());

        Random random = new Random();
        int x = random.nextInt(0, 200);
        int y = random.nextInt(0, 200);

        Player newPlayer = new Player(UUID.randomUUID().toString(), player.getUsername(), x, y);
        players.add(newPlayer);

        sessionIdToPlayerUuid.put(headerAccessor.getSessionId(), newPlayer.getUuid());

        // send new player to the specific client
        messagingTemplate.convertAndSendToUser(headerAccessor.getUser().getName(), "/for-specific-client/player", newPlayer);

        // send new player to all clients
        messagingTemplate.convertAndSend("/for-all-clients/new-player", newPlayer);
    }
}
