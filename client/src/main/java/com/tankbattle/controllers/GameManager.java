package com.tankbattle.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        if (username.length() == 0) {

            username = "Guest#" + UUID.randomUUID().toString().substring(0, 4);
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
                            (float) ((Map<String, Integer>) playerData.get("location")).get("x"),
                            (float) ((Map<String, Integer>) playerData.get("location")).get("y"));
                    Vector2 size = new Vector2(
                            (float) ((Map<String, Integer>) playerData.get("size")).get("x"),
                            (float) ((Map<String, Integer>) playerData.get("size")).get("y"));

                    if (currentPlayer.getUsername().equals(username)) {
                        currentPlayer.setLocation(location);
                        currentPlayer.setSize(size);
                    } else if (this.players.containsKey(username)) {
                        this.players.get(username).setLocation(location);
                        this.players.get(username).setSize(size);
                    } else {
                        this.players.put(username, new Player(username, location, size));
                    }

                });

        for (Player player : this.players.values()) {
            System.out.println(player.toString());
        }
    }

    public void update() {
        GameWindow.getInstance().getGamePanel().repaint();
        byte movementDirection = currentPlayer.getMovementDirection();
        byte previousDirection = currentPlayer.getPreviousDirection();

        if (movementDirection != 0) {
            webSocketManager.sendMovementDirection(movementDirection);
        } else if (previousDirection != 0 && movementDirection == 0) {
            webSocketManager.sendMovementDirection(movementDirection);
            currentPlayer.setPreviousDirection((byte) 0);
        }
    }
}
