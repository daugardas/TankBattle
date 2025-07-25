package com.tankbattle.controllers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import com.tankbattle.commands.FireCommand;
import com.tankbattle.commands.ICommand;
import com.tankbattle.commands.MoveCommand;
import com.tankbattle.input.InputData;
import com.tankbattle.models.Bullet;
import com.tankbattle.models.Collision;
import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Level;
import com.tankbattle.models.Player;
import com.tankbattle.models.PowerUp;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.RenderFacade;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();
    private ScheduledExecutorService movementCommandExecutorService;
    private WebSocketManager webSocketManager;
    private RenderFacade renderFacade;
    private ResourceManager resourceManager;
    private Thread drawRequestThread;
    private final ConcurrentHashMap<String, Player> players;
    private final CopyOnWriteArrayList<Bullet> bullets;
    private final CopyOnWriteArrayList<PowerUp> powerUps;
    public int playerCount = 0;
    private Level level;
    private CurrentPlayer currentPlayer;
    private boolean gameRunning = false;

    private final List<ICommand> commands = new ArrayList<>();

    private GameManager() {
        webSocketManager = new WebSocketManager();
        resourceManager = new ResourceManager();
        renderFacade = new RenderFacade(resourceManager);

        players = new ConcurrentHashMap<>();
        bullets = new CopyOnWriteArrayList<>();
        powerUps = new CopyOnWriteArrayList<>();
        level = new Level();
    }

    private void resetDrawingThread() {
        System.out.println("reseting drawing request thread");
        // use another thread to request redraws continuously,
        // and swing will redraw the screen as soon as it can.
        // This should increase the fps,
        // as there would be no need for a fixed timer.
        this.drawRequestThread = new Thread(() -> {
            System.out.println("Draw request thread started");
            while (gameRunning) {
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
        System.out.println("Starting game");
        GameWindow.getInstance().initializeGameScreen();
        this.gameRunning = true;
        resetDrawingThread();
        this.drawRequestThread.start();

        movementCommandExecutorService = Executors
        .newSingleThreadScheduledExecutor();

        movementCommandExecutorService.scheduleAtFixedRate(this::updateInput, 0, 16, TimeUnit.MILLISECONDS);
    }

    public void stopGame() {
        players.clear();
        bullets.clear();
        level = new Level();
        this.webSocketManager = new WebSocketManager();
        drawRequestThread = null;
        this.gameRunning = false;
        movementCommandExecutorService.shutdownNow();
        movementCommandExecutorService = Executors
        .newSingleThreadScheduledExecutor();
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
        this.renderTiles(g2d);
        this.renderPlayers(g2d);
        this.renderBullets(g2d);
        this.renderPowerUps(g2d);
        this.renderExplosions(g2d);
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

    private void renderPowerUps(Graphics2D g2d) {
        renderFacade.drawEntities(g2d, powerUps);
    }

    private void renderExplosions(Graphics2D g2d) {
        List<Collision> explosions = CollisionManager.getInstance().getExplosions();
        renderFacade.drawEntities(g2d, explosions);
    }

    public void shutdown() {
        movementCommandExecutorService.shutdown();
        webSocketManager.close();
    }

    public RenderFacade getRenderFacade() {
        return renderFacade;
    }

    public void updatePowerUps(ArrayList<PowerUp> incomingPowerUps) {
        this.powerUps.clear();
        this.powerUps.addAll(incomingPowerUps);
    }
}
