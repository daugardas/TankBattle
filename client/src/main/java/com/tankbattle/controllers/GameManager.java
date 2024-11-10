package com.tankbattle.controllers;

import com.tankbattle.models.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.tankbattle.commands.MoveCommand;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.RenderFacade;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {
    private final ExecutorService movementCommandExecutorService = Executors.newSingleThreadExecutor();
    private final WebSocketManager webSocketManager;
    private final RenderFacade renderFacade;
    private final ResourceManager resourceManager;

    private final Thread drawRequestThread;

    private Level level;
    private final HashMap<String, Player> players;
    private final ArrayList<Bullet> bullets;
    private CurrentPlayer currentPlayer;
    public int playerCount = 0;

    private GameManager() {
        webSocketManager = new WebSocketManager();
        resourceManager = new ResourceManager();
        renderFacade = new RenderFacade(resourceManager);

        players = new HashMap<>();
        bullets = new ArrayList<>();
        level = new Level();

        // use another thread to request redraws continuously, 
        // and swing will redraw the screen as soon as it can.
        // This should increase the fps, 
        // as there would be no need for a fixed timer.
        this.drawRequestThread = new Thread(() -> {
            while (true) {
                // asks game panel to repaint only when swing is ready
                SwingUtilities.invokeLater(() -> {
                    GameWindow.getInstance().getGamePanel().repaint();
                });

                try {
                    Thread.sleep(1); // sleep to avoid high cpu usage
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
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

        drawRequestThread.start();

        Timer timer = new Timer(16, event -> this.update());
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
        this.level = level;
        GameWindow.getInstance().setGamePanelWorldSize(level.getWidth() * 1000, level.getHeight() * 1000);
    }

    public Level getLevel() {
        return level;
    }

    public void addPlayers(List<Player> incomingPlayers) {
        // no need to offload the players update to another thread as the 
        // amount of players is so small that creating a new thread would be
        // more expensive than just updating the players in the main thread.
        // This was confirmed with testing.
        Set<String> incomingUsernames = new HashSet<>();

            for (Player player : incomingPlayers) {
                incomingUsernames.add(player.getUsername());

                // checking if player is current client player
                if (currentPlayer.getUsername().equals(player.getUsername())){
                    this.currentPlayer.setLocation(player.getLocation());
                    this.currentPlayer.setSize(player.getSize());
                    this.currentPlayer.setRotationAngle(player.getRotationAngle());
                }
                else {
                    Player existingPlayer = this.players.get(player.getUsername());
                    if (existingPlayer != null) {
                        existingPlayer.setLocation(player.getLocation());
                        existingPlayer.setSize(player.getSize());
                        existingPlayer.setRotationAngle(player.getRotationAngle());
                    } else {
                    this.players.put(player.getUsername(), player);
                    }
                }
            }

            if (this.players.size() + 1 != incomingPlayers.size())
                this.players.keySet().removeIf(username -> !incomingUsernames.contains(username));
    }

    public void updateBullets(ArrayList<Bullet> bullets) {
        this.bullets.clear();
        this.bullets.addAll(bullets);
    }

    public void clearBullets() {
        this.bullets.clear();
    }

    public void update() {      
        movementCommandExecutorService.execute(() -> {
            this.updatePlayerMovement();
        });
        
    }

    private void updatePlayerMovement() {
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

    public void renderAll(Graphics2D g2d) {
        this.renderTiles(g2d);
        this.renderPlayers(g2d);
        this.renderBullets(g2d);
    }

    private void renderTiles(Graphics2D g2d) {
        Tile[][] tileGrid = level.getGrid();
        if (tileGrid != null) {
            for (Tile[] row : tileGrid) {
                renderFacade.drawEntities(g2d, Arrays.asList(row));
            }
        }
    }

    private void renderPlayers(Graphics2D g2d) {
        for (Player player : players.values()) {
            renderFacade.drawEntity(g2d, player);
        }
        renderFacade.drawEntity(g2d, currentPlayer);
    }

    private void renderBullets(Graphics2D g2d) {
        renderFacade.drawEntities(g2d, bullets);
    }

    public void shutdown() {
        movementCommandExecutorService.shutdown();
        webSocketManager.close();
    }
}
