package com.tankbattle.controllers;

import java.util.HashMap;

import com.tankbattle.models.Player;

public class GameManager {

    private WebSocketManager webSocketManager;
    private HashMap<String, Player> players;
    public int playerCount = 0;

    private GameManager() {
        webSocketManager = new WebSocketManager();
        players = new HashMap<>();
    }

    private static final GameManager INSTANCE = new GameManager();

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public void connectToServer(String hostname, String username) {
        webSocketManager.connect(hostname, username);
    }

    public void addPlayers(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            Player temp = players[i];

            if (this.players.containsKey(temp.getUsername())) {
                Player player = this.players.get(temp.getUsername());
                player.setLocation(temp.getLocation());
            } else {
                this.players.put(temp.getUsername(), temp);
            }
        }

        for (Player player : this.players.values()) {
            System.out.println(player.toString());
        }
    }

}
