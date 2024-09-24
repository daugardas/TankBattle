package com.tankbattle.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Player;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {

    private WebSocketManager webSocketManager;
    private GameWindow gameWindow;
    private HashMap<String, Player> players;
    private CurrentPlayer currentPlayer;
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
        if (username.length() == 0) {
            username = String.format("Guest%d", GameManager.getInstance().playerCount + 1);
        }

        webSocketManager.connect(hostname, username);
        currentPlayer = new CurrentPlayer(username);
    }

    public void addPlayers(Object[] o) {
        Arrays.stream(o)
                .forEach(player -> {
                    Map<String, Object> playerData = (Map<String, Object>) player;

                    String username = (String) playerData.get("username");
                    Vector2 location = new Vector2(
                            (Integer) ((Map<String, Integer>) playerData.get("location")).get("x"),
                            (Integer) ((Map<String, Integer>) playerData.get("location")).get("y"));

                    if (currentPlayer.getUsername().equals(username)) {
                        currentPlayer.setLocation(location);
                    } else if (this.players.containsKey(username)) {
                        this.players.get(username).setLocation(location);
                    } else {
                        this.players.put(username, new Player(username, location));
                    }

                });

        for (Player player : this.players.values()) {
            System.out.println(player.toString());
        }
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> allPlayers = new ArrayList<>(this.players.values());
        allPlayers.add(currentPlayer);

        return allPlayers;

    }

    public void sendMovementBuffer(int[] movementBuffer) {
        webSocketManager.sendMovementBuffer(movementBuffer);
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void addGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void startGame() {
        gameWindow.initializeGameScreen();
    }
}
