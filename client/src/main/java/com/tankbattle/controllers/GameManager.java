package com.tankbattle.controllers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.Timer;

import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Player;
import com.tankbattle.renderers.RendererManager;
import com.tankbattle.renderers.TankRenderer;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {

    private final WebSocketManager webSocketManager;
    private final RendererManager rendererManager;
    private final ResourceManager resourceManager;
    private final HashMap<String, Player> players;
    private CurrentPlayer currentPlayer;
    public int playerCount = 0;
    private double scaleFactor = 4.0;

    private GameManager() {
        webSocketManager = new WebSocketManager();
        rendererManager = new RendererManager();
        resourceManager = new ResourceManager();
        players = new HashMap<>();

        TankRenderer tankRenderer = new TankRenderer(scaleFactor, resourceManager);
        rendererManager.registerRenderer(Player.class, tankRenderer);
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

        Timer timer = new Timer(33, event -> this.update());
        timer.start();
    }

    private void connectToServer(String hostname, String username) {
        if (username.length() == 0) {

            username = "Guest#" + UUID.randomUUID().toString().substring(0, 4);
        }

        webSocketManager.connect(hostname, username);
        currentPlayer = new CurrentPlayer(username, new Vector2(0, 0),
                new Vector2(10, 10), Color.BLACK, Color.RED);
    }

    public void addPlayers(Object[] o) {
        Set<String> incomingUsernames = new HashSet<>();
    
        Arrays.stream(o)
                .forEach(player -> {
                    Map<String, Object> playerData = (Map<String, Object>) player;
    
                    String username = (String) playerData.get("username");
    
                    Map<String, Number> locationMap = (Map<String, Number>) playerData.get("location");
                    Map<String, Number> sizeMap = (Map<String, Number>) playerData.get("size");
    
                    Vector2 location = new Vector2(
                            locationMap.get("x").intValue(),
                            locationMap.get("y").intValue());
                    Vector2 size = new Vector2(
                            sizeMap.get("x").intValue(),
                            sizeMap.get("y").intValue());
    
                    double rotationAngle = ((Number) playerData.get("rotationAngle")).doubleValue();
    
                    incomingUsernames.add(username);
    
                    if (currentPlayer.getUsername().equals(username)) {
                        currentPlayer.setLocation(location);
                        currentPlayer.setSize(size);
                        currentPlayer.setRotationAngle(rotationAngle);
                    } else if (this.players.containsKey(username)) {
                        Player otherPlayer = this.players.get(username);
                        otherPlayer.setLocation(location);
                        otherPlayer.setSize(size);
                        otherPlayer.setRotationAngle(rotationAngle);
                    } else {
                        Player newPlayer = new Player(username, location, size, Color.BLACK, Color.GREEN);
                        newPlayer.setRotationAngle(rotationAngle);
                        this.players.put(username, newPlayer);
                    }
                });
    
    if (players.size() + 1 != o.length)
        players.keySet().removeIf(username -> !incomingUsernames.contains(username));
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

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        rendererManager.setScaleFactor(scaleFactor);
    }

    public void renderAll(Graphics2D g2d) {
        List<Player> allPlayers = getAllPlayers();
        for (Player player : allPlayers) {
            rendererManager.draw(g2d, player);
        }
        // same for things like bullets, powerups, map elements etc.
    }
}
