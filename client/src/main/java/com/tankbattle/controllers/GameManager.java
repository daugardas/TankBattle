package com.tankbattle.controllers;

import com.tankbattle.commands.FireCommand;
import com.tankbattle.commands.ICommand;
import com.tankbattle.commands.MoveCommand;
import com.tankbattle.input.InputData;
import com.tankbattle.models.Bullet;
import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.RenderFacade;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();
    private final ScheduledExecutorService movementCommandExecutorService = Executors
            .newSingleThreadScheduledExecutor();
    private final WebSocketManager webSocketManager;
    private final RenderFacade renderFacade;
    private final ResourceManager resourceManager;
    private final Thread drawRequestThread;
    private final ConcurrentHashMap<String, Player> players;
    private final CopyOnWriteArrayList<Bullet> bullets;
    public int playerCount = 0;
    private Level level;
    private CurrentPlayer currentPlayer;

    private final List<ICommand> commands = new ArrayList<>();

    private GameManager() {
        webSocketManager = new WebSocketManager();
        resourceManager = new ResourceManager();
        renderFacade = new RenderFacade(resourceManager);

        players = new ConcurrentHashMap<>();
        bullets = new CopyOnWriteArrayList<>();
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

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        GameWindow.getInstance().setVisible(true);
    }

    public boolean connectToServer(String url, String username) {
        return webSocketManager.connect(url, username);
    }

    public void setUsername(String username) {
        this.currentPlayer = new CurrentPlayer(username);
    }

    public void startGame() {
        GameWindow.getInstance().initializeGameScreen();

        drawRequestThread.start();

        movementCommandExecutorService.scheduleAtFixedRate(this::updateInput, 0, 16, TimeUnit.MILLISECONDS);
    }

    public void setLevel(Level level) {
        this.level = level;
        GameWindow.getInstance().setGamePanelWorldSize(level.getWidth() * 1000, level.getHeight() * 1000);
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
            if (this.currentPlayer.getUsername().equals(player.getUsername())) {
                this.currentPlayer.setTank(player.getTank());
            } else {
                Player existingPlayer = this.players.get(player.getUsername());
                if (existingPlayer != null) {
                    existingPlayer.setTank(player.getTank());
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

    private void updateInput() {
        InputData inputData = currentPlayer.getInputData();

        processMovement(inputData);
        processActions(inputData);
        sendCommands();
    }

    private void processMovement(InputData inputData) {
        currentPlayer.setPreviousDirection(currentPlayer.getMovementDirection());
        byte previousDirection = currentPlayer.getPreviousDirection();
        byte movementDirection = inputData.getMovementDirection();
        currentPlayer.setMovementDirection(movementDirection);

        if (movementDirection != 0) {
            MoveCommand moveCommand = new MoveCommand(movementDirection);
            addCommand(moveCommand);
        } else if (previousDirection != 0) {
            MoveCommand moveCommand = new MoveCommand(movementDirection);
            addCommand(moveCommand);
            currentPlayer.setPreviousDirection((byte) 0);
        }
    }

    private void processActions(InputData inputData) {
        if (inputData.getActions().containsKey("FIRE")) {
            addCommand(new FireCommand());
        }
    }

    public void sendCommands() {
        for (ICommand command : commands) {
            webSocketManager.sendCommand(command);
        }
        commands.clear();
    }

    public void addCommand(ICommand command) {
        commands.add(command);
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
        // System.out.println("Rendering all game entities through RenderFacade.");
        this.renderTiles(g2d);
        this.renderPlayers(g2d);
        this.renderBullets(g2d);
    }

    private void renderTiles(Graphics2D g2d) {
        // System.out.println("Rendering tiles through RenderFacade.");
        Tile[][] tileGrid = level.getGrid();
        if (tileGrid != null) {
            for (Tile[] row : tileGrid) {
                renderFacade.drawEntities(g2d, Arrays.asList(row));
            }
        }
    }

    private void renderPlayers(Graphics2D g2d) {
        // System.out.println("Rendering players through RenderFacade.");
        for (Player player : players.values()) {
            renderFacade.drawEntity(g2d, player);
        }

        renderFacade.drawEntity(g2d, currentPlayer);

    }

    private void renderBullets(Graphics2D g2d) {
        // System.out.println("Rendering bullets through RenderFacade.");
        renderFacade.drawEntities(g2d, bullets);
    }

    public void shutdown() {
        movementCommandExecutorService.shutdown();
        webSocketManager.close();
    }
}
