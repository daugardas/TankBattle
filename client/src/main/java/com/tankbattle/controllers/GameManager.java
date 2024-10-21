package com.tankbattle.controllers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.Timer;

import com.tankbattle.models.Collision;
import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.ExplosionRenderer;
import com.tankbattle.renderers.RendererManager;
import com.tankbattle.renderers.TankRenderer;
import com.tankbattle.renderers.TileRenderer;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {

    private final WebSocketManager webSocketManager;
    private final RendererManager rendererManager;
    private final ResourceManager resourceManager;

    private Level level;
    private final HashMap<String, Player> players;
    private CurrentPlayer currentPlayer;
    public int playerCount = 0;
    private final List<Collision> collisions = new ArrayList<>();
    private static final long COLLISION_LIFETIME = 1000; // 5 seconds

    private GameManager() {
        webSocketManager = new WebSocketManager();
        rendererManager = new RendererManager();
        resourceManager = new ResourceManager();
        players = new HashMap<>();
        level = new Level();

        TankRenderer tankRenderer = new TankRenderer(resourceManager);
        rendererManager.registerRenderer(Player.class, tankRenderer);
        rendererManager.registerRenderer(Tile.class, new TileRenderer(resourceManager));
        rendererManager.registerRenderer(Collision.class, new ExplosionRenderer(resourceManager));
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

    public void setLevel(Level level) {
        System.out.println("Received level update: " + level);
        this.level = level;

        GameWindow.getInstance().setGamePanelWorldSize(level.getWidth() * 1000, level.getHeight() * 1000);
    }

    public Level getLevel() {
        return level;
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

    public void setRenderingScaleFactor(float scaleFactor) {
        rendererManager.setRenderingScaleFactor(scaleFactor);
    }

    public void setWorldLocationScaleFactor(float scaleFactor) {
        rendererManager.setWorldLocationScaleFactor(scaleFactor);
    }

    public void setWorldOffset(Vector2 worldOffset) {
        rendererManager.setWorldOffset(worldOffset);
    }

    public void addCollision(Vector2 location) {
        collisions.add(new Collision(location));
    }
    
    public List<Collision> getCollisions() {
        return new ArrayList<>(collisions);
    }

    public void renderAll(Graphics2D g2d) {
        // level rendering
        Tile[][] tileGrid = level.getGrid();
        if (tileGrid != null) {
            for (Tile[] row : tileGrid) {
                for (Tile tile : row) {
                    rendererManager.draw(g2d, tile);
                }
            }
        }

        List<Player> allPlayers = getAllPlayers();
        for (Player player : allPlayers) {
            rendererManager.draw(g2d, player);
        }


        Iterator<Collision> iterator = collisions.iterator();
    long currentTime = System.currentTimeMillis();
    while (iterator.hasNext()) {
        Collision collision = iterator.next();
        if (currentTime - collision.getTimestamp() > COLLISION_LIFETIME) {
            iterator.remove(); // Remove old collisions
        } else {
            rendererManager.draw(g2d, collision);
        }
    }

    }
}
