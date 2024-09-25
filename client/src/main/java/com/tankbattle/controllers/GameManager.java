package com.tankbattle.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Player;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {

    private WebSocketManager webSocketManager;
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

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> allPlayers = new ArrayList<>(this.players.values());
        allPlayers.add(currentPlayer);

        return allPlayers;
    }

    public void initialize() {
        GameWindow.getInstance().setVisible(true);
    }

    public void startGame(String url, String username) {
        connectToServer(url, username);
        GameWindow.getInstance().initializeGameScreen();

        Timer timer = new Timer(33, e -> this.update());
        timer.start();
    }

    private void connectToServer(String hostname, String username) {
        try {
            if (username.length() == 0) {
                username = InetAddress.getLocalHost().getHostName();
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
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

    public void update() {
        GameWindow.getInstance().getGamePanel().repaint();
        int[] movementBuffer = currentPlayer.getMovementBuffer();

        if (movementBuffer[0] != 0 || movementBuffer[1] != 0) {
            webSocketManager.sendMovementBuffer(movementBuffer);
        }
    }

    public void sendMovementBuffer(int[] movementBuffer) {
        webSocketManager.sendMovementBuffer(movementBuffer);
    }

}
