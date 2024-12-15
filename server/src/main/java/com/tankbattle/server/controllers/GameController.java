package com.tankbattle.server.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tankbattle.server.builders.BasicLevelBuilder;
import com.tankbattle.server.commands.FireCommand;
import com.tankbattle.server.commands.ICommand;
import com.tankbattle.server.commands.MoveCommand;
import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.factories.LevelGeneratorFactory;
import com.tankbattle.server.interpreter.CommandConsoleWindow;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.ICollidableEntity;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.items.BasicItemFactory;
import com.tankbattle.server.models.items.ItemFactory;
import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.strategies.Level.LevelGenerator;
import com.tankbattle.server.utils.Vector2;

import jakarta.annotation.PostConstruct;

@Controller
public class GameController {

    /*
     * Level consists of tiles, which indicated the width and height of the world.
     * Coordinate system will assume that a tile has 1000 units of width and height,
     * so a 20x20 level will be 20000x20000 coordinate units.
     *
     * This will ensure smooth movement of players and objects in the world, and
     * still make it possible to have the level be made up of a limited number of
     * tiles.
     *
     * It will be easy to convert between tile coordinates and world coordinates, as
     * the conversion factor is 1000.
     *
     */

    public static final int TILE_HEIGHT = 1000;
    public static final int TILE_WIDTH = 1000;
    public static final int WORLD_HEIGHT = 10;
    public static final int WORLD_WIDTH = 10;

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CollisionManager collisionManager;

    private CommandConsoleWindow commandConsoleWindow;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<ITank> tanks = new ArrayList<>();
    private final ArrayList<Bullet> bullets = new ArrayList<>();

    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private ArrayList<PowerDown> powerDowns = new ArrayList<>();
    private ItemFactory itemFactory = new BasicItemFactory();

    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

    private ArrayList<ICommand> commands = new ArrayList<>();

    private boolean run = true;
    private ArrayList<ICommand> commandsLog = new ArrayList<>();

    private Level level;

    public Level getLevel() {
        return level;
    }

    public int getLevelCoordinateWidth() {
        return level.getWidth() * TILE_WIDTH;
    }

    public int getLevelCoordinateHeight() {
        return level.getHeight() * TILE_HEIGHT;
    }

    // allows printing to the console window for other classes
    // in the server who have access to GameController
    public void printToConsole(String message) {
        if (commandConsoleWindow != null) {
            SwingUtilities.invokeLater(() -> {
                commandConsoleWindow.printToConsole(message);
            });
        }
    }

    public void printHelpToConsole() {
        if (commandConsoleWindow != null) {
            SwingUtilities.invokeLater(() -> {
                commandConsoleWindow.showHelp();
            });
        }
    }

    public void movePlayer(String username, float x, float y) {
        for (Player player : players) {
            if (player.getUsername().equals(username)) {
                player.getTank().getLocation().addToX(x);
                player.getTank().getLocation().addToY(y);
                break;
            }
        }
    }

    public void kickPlayer(String username) {
        System.out.println("Removing '" + username + "' from server");
        String sessionId = players.stream().filter(player -> player.getUsername().equals(username))
                .map(Player::getSessionId).findFirst().orElse(null);
        if (sessionId == null) {
            System.out.println("Couldn't find online player with username '" + username + "'");
            printToConsole("Couldn't find online player with username '" + username + "'");
            return;
        }

        if (!sessionManager.isSessionActive(sessionId)) {
            System.out.println("Session '" + sessionId + "' is not active.");
            return;
        }

        System.out.println("Removing session '" + sessionId + "'");
        sessionManager.removeSession(sessionId);
        removePlayerBySessionId(sessionId);
        printToConsole("Kicked '" + username + "' from the server.");
    }

    @PostConstruct
    public void init() {
        SwingUtilities.invokeLater(() -> {
            try {
                commandConsoleWindow = new CommandConsoleWindow(this);
                commandConsoleWindow.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to create command console: " + e.getMessage());
            }

        });

        LevelGenerator generator;
        boolean useProceduralGeneration = false;

        if (useProceduralGeneration) {
            generator = LevelGeneratorFactory.createGenerator("random");
        } else {
            generator = LevelGeneratorFactory.createGenerator("prebuilt");
        }

        var levelBuilder = new BasicLevelBuilder(generator);
        levelBuilder.generateLevel().addSpawnPoints(4).addPowerUps(10);
        level = levelBuilder.build();

        printToConsole("Level initialized. Level:");
        printToConsole(level.toString());

        spawnItemsAtLocations();

        // -----------------------------------------Prototype------------------------------------------------------
        /*
         *
         * Tile tile = new IceTile();
         * Tile copy = (IceTile) tile.copyShallow();
         *
         * System.out.println("New element hash code(shallowCopy):" +
         * System.identityHashCode(tile.getHealth()));
         * System.out.println("Old element hash code(shallowCopy):" +
         * System.identityHashCode(copy.getHealth()));
         *
         * Tile tileOG = new IceTile();
         * Tile tileCopy = tile.copyDeep();
         *
         * System.out.println("New element hash code(deepCopy):" +
         * System.identityHashCode(tileOG));
         * System.out.println("Old element hash code(deepCopy):" +
         * System.identityHashCode(tileCopy));
         *
         */
        // -----------------------------------------Prototype------------------------------------------------------

        collisionManager.initializeStaticEntities(level);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void addPlayer(Player player) {
        sessionIdToPlayerIndex.put(player.getSessionId(), this.players.size());

        // assign player starting location
        int existingPlayers = this.players.size();
        Vector2 spawnLocation = level.getSpawnPoints()[existingPlayers];
        
        // Create new player with spawn location
        player = new Player(player.getSessionId(), player.getUsername(), spawnLocation);
        player.getTank().setLocationToTile(spawnLocation);

        this.players.add(player);
        this.tanks.add(player.getTank());
        collisionManager.spatialGrid.addEntity(player.getTank(), false);
    }

    public void removePlayerBySessionId(String sessionId) {
        Player playerToRemove = null;
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getSessionId().equals(sessionId)) {
                playerToRemove = player;
                iterator.remove();
                break;
            }
        }

        if (playerToRemove != null) {
            collisionManager.spatialGrid.removeEntity(playerToRemove.getTank());
        }

        sessionIdToPlayerIndex.remove(sessionId);

        // Rebuild the sessionIdToPlayerIndex map
        sessionIdToPlayerIndex.clear();
        for (int i = 0; i < players.size(); i++) {
            sessionIdToPlayerIndex.put(players.get(i).getSessionId(), i);
        }
    }

    @SubscribeMapping("/level")
    // send the initial level when client starts listening to /server/level
    public Level sendLevel() {
        // System.out.println("Subscribed to level");
        // messagingTemplate.convertAndSend("/server/level", level);

        // workaround for sending the hard-coded map, as the generation sometimes
        // crashes
        ObjectMapper mapper = new ObjectMapper();
        try {
            String levelJson = mapper.writeValueAsString(level);
            System.out.println("Sending level: " + levelJson);
            return level;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Scheduled(fixedRate = 33)
    public void gameLoop() {

        // if (run) {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).execute();
        }

        commands.clear();
        // } else {
        // if (commandsLog.size() > 0) {
        // commandsLog.get(commandsLog.size() - 1).undo();
        // commandsLog.remove(commandsLog.size() - 1);
        // }
        // else{
        // run = true;
        // }
        // }

        updatePlayersLocations();
        updateBulletsLocations();

        // Detect and handle collisions
        collisionManager.detectCollisions(tanks, bullets, powerUps, powerDowns);

        // Broadcast updated game state to clients
        broadcastGameState();

    }

    private void updatePlayersLocations() {
        for (Player player : players) {
            player.getTank().updateLocation();
            collisionManager.spatialGrid.updateEntity(player.getTank());
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void removeCollidedBullet(Bullet bullet) {
        collisionManager.spatialGrid.removeEntity(bullet);
        bullets.remove(bullet);
    }

    private void updateBulletsLocations() {
        for (Bullet bullet : bullets) {
            bullet.updatePosition();
            collisionManager.spatialGrid.updateEntity(bullet);
        }
    }

    private void broadcastGameState() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            messagingTemplate.convertAndSend("/server/players", players);
            messagingTemplate.convertAndSend("/server/bullets", bullets);
            messagingTemplate.convertAndSend("/server/powerups", powerUps);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed" + e.getMessage());
        }
    }

    @MessageMapping("/command")
    public void processCommand(@Payload Map<String, Object> command, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Integer playerIndex = sessionIdToPlayerIndex.get(sessionId);

        if (playerIndex != null && playerIndex < players.size()) {

            String type = command.get("type").toString();

            switch (type) {
                case "MOVE":
                    MoveCommand moveCommand = new MoveCommand(players.get(playerIndex).getTank(),
                            ((Integer) command.get("direction")).byteValue());

                    if (!commands.contains(moveCommand) /* && run */) {
                        commands.add(moveCommand);
                        commandsLog.add(moveCommand);

                        // if (commandsLog.size() >= 100) {
                        // run = false;
                        // }
                    }
                    break;
                case "FIRE":
                    Player currentPlayer = players.get(playerIndex);
                    FireCommand fireCommand = new FireCommand(currentPlayer.getTank(), currentPlayer);
                    if (!commands.contains(fireCommand)) {
                        commands.add(fireCommand);
                    }
                    break;

                default:
                    break;
            }

        } else {
            System.err.println("Invalid session ID or player index: " + sessionId);
        }
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void sendCollisionLocation(int x, int y) {
        Vector2 collisionLocation = new Vector2(x, y);

        messagingTemplate.convertAndSend("/server/collisions", collisionLocation);
    }

    public void spawnItemsAtLocations() {
        List<Vector2> locations = Arrays.asList(
                new Vector2(500, 2500),
                new Vector2(2500, 9500),
                new Vector2(9500, 2500),
                new Vector2(7500, 9500));

        PowerUp powerUp1 = itemFactory.createSpeedPowerUp(locations.get(0));
        addPowerUp(powerUp1);
        PowerUp powerUp2 = itemFactory.createSpeedPowerUp(locations.get(1));
        addPowerUp(powerUp2);
        PowerUp powerUp3 = itemFactory.createArmorPowerUp(locations.get(2));
        addPowerUp(powerUp3);
        PowerUp powerUp4 = itemFactory.createArmorPowerUp(locations.get(3));
        addPowerUp(powerUp4);

        // PowerDown powerDown1 = itemFactory.createSpeedPowerDown(locations.get(0));
        // addPowerDown(powerDown1);
        // PowerDown powerDown2 = itemFactory.createHealthPowerDown(locations.get(1));
        // addPowerDown(powerDown2);
        // PowerDown powerDown3 = itemFactory.createArmorPowerDown(locations.get(2));
        // addPowerDown(powerDown3);
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
        collisionManager.spatialGrid.addEntity(powerUp, false);
    }

    public void addPowerDown(PowerDown powerDown) {
        powerDowns.add(powerDown);
        collisionManager.spatialGrid.addEntity(powerDown, false);
    }

    public void updateTankReference(ITank oldTank, ITank newTank) {
        for (Player player : players) {
            if (player.getTank() == oldTank) {
                player.setTank(newTank);
                break;
            }
        }

        int index = tanks.indexOf(oldTank);
        if (index != -1) {
            tanks.set(index, newTank);
        }

        collisionManager.spatialGrid.removeEntity((ICollidableEntity) oldTank);
        collisionManager.spatialGrid.addEntity((ICollidableEntity) newTank, false);

        System.out.println("Tank reference updated");
    }

    public List<Tank> getTanks() {
        List<Tank> tankList = new ArrayList<>();
        for (ITank tank : tanks) {
            tankList.add(tank.getTank());
        }
        return tankList;
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
        collisionManager.spatialGrid.removeEntity(powerUp);
    }

    public void removePowerDown(PowerDown powerDown) {
        powerDowns.remove(powerDown);
        collisionManager.spatialGrid.removeEntity(powerDown);
    }

    public void notifyTankDestroyed(Vector2 location) {
        messagingTemplate.convertAndSend("/server/tank-destroyed", location);
    }

}
