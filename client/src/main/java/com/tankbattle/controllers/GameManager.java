package com.tankbattle.controllers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;

import javax.swing.Timer;

import com.tankbattle.models.Collision;
import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.RenderFacade;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

import com.tankbattle.commands.*;

public class GameManager {

    private final WebSocketManager webSocketManager;
    private final RenderFacade renderFacade;
    private final ResourceManager resourceManager;

    private Level level;
    private final HashMap<String, Player> players;
    private CurrentPlayer currentPlayer;
    public int playerCount = 0;
    private static final long COLLISION_LIFETIME = 1000; // 5 seconds

    // FPS for server updates
    private long serverUpdateCount = 0;
    private float serverFps = 0;
    private long serverFpsTimerStart = System.currentTimeMillis();

    private GameManager() {
        webSocketManager = new WebSocketManager();
        resourceManager = new ResourceManager();
        renderFacade = new RenderFacade(resourceManager);
        players = new HashMap<>();
        level = new Level();

        Timer serverFpsTimer = new Timer(1000, e -> updateServerFps());
        serverFpsTimer.start();
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

        Timer timer = new Timer(1, event -> this.update());
        timer.start();
    }

    private void connectToServer(String hostname, String username) {
        if (username.isEmpty()) {
            username = "Guest#" + UUID.randomUUID().toString().substring(0, 4);
        }

        webSocketManager.connect(hostname, username);
        currentPlayer = new CurrentPlayer(username, new Vector2(0, 0), new Vector2(10, 10), Color.BLACK, Color.RED);
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

        MoveCommand moveCommand = new MoveCommand(movementDirection);

        if (movementDirection != 0) {
            webSocketManager.sendCommand(moveCommand);
        } else if (previousDirection != 0 && movementDirection == 0) {
            webSocketManager.sendCommand(moveCommand);
            currentPlayer.setPreviousDirection((byte) 0);
        }
    }

    public void setRenderingScaleFactor(float scaleFactor) {
        renderFacade.setRenderingScaleFactor(scaleFactor);
    }

    public void setWorldLocationScaleFactor(float scaleFactor) {
        renderFacade.setWorldLocationScaleFactor(scaleFactor);
    }

    public void setWorldOffset(Vector2 worldOffset) {
        renderFacade.setWorldOffset(worldOffset);
    }

    private final Set<Vector2> activeCollisionLocations = new HashSet<>();
    private final List<Collision> collisions = new ArrayList<>();

    public void addCollision(Vector2 location) {
        Vector2 collisionLocation = new Vector2(location.getX(), location.getY());
        if (activeCollisionLocations.contains(collisionLocation)) {
            return;
        }
        activeCollisionLocations.add(collisionLocation);
        collisions.add(new Collision(collisionLocation));
    }

    public List<Collision> getCollisions() {
        return new ArrayList<>(collisions);
    }

    public void renderAll(Graphics2D g2d) {
        // Render level tiles
        Tile[][] tileGrid = level.getGrid();
        if (tileGrid != null) {
            for (Tile[] row : tileGrid) {
                renderFacade.drawEntities(g2d, Arrays.asList(row));
            }
        }

        // Render all players
        renderFacade.drawEntities(g2d, getAllPlayers());

        // Render collisions with lifetime check
        long currentTime = System.currentTimeMillis();
        Iterator<Collision> iterator = collisions.iterator();
        while (iterator.hasNext()) {
            Collision collision = iterator.next();
            if (currentTime - collision.getTimestamp() > COLLISION_LIFETIME) {
                activeCollisionLocations.remove(collision.getLocation());
                iterator.remove();
            } else {
                renderFacade.drawEntity(g2d, collision);
            }
        }
    }

    public void incrementServerUpdateCount() {
        serverUpdateCount++;
    }

    private void updateServerFps() {
        long currentTime = System.currentTimeMillis();
        serverFps = (serverUpdateCount * 1000.0f) / (currentTime - serverFpsTimerStart);
        serverFpsTimerStart = currentTime;
        serverUpdateCount = 0;
    }

    public float getServerFps() {
        return serverFps;
    }
}
