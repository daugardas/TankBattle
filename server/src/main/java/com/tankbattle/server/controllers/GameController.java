package com.tankbattle.server.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.BasicPowerUpFactory;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpFactory;
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

    private List<Player> players = new ArrayList<>();
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

    private List<ICommand> commands = new ArrayList<>();
    private List<ICommand> commandLog = new ArrayList<>();

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

    @PostConstruct
    public void init() {
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

        System.out.println("Level initialized. Level:");
        System.out.println(level.toString());

        spawnPowerUpsAtLocations();

        //-----------------------------------------Prototype------------------------------------------------------
        /*

        Tile tile = new IceTile();
        Tile copy = (IceTile) tile.copyShallow();

        System.out.println("New element hash code(shallowCopy):" + System.identityHashCode(tile.getHealth()));
        System.out.println("Old element hash code(shallowCopy):" + System.identityHashCode(copy.getHealth()));

        Tile tileOG = new IceTile();
        Tile tileCopy = tile.copyDeep();

        System.out.println("New element hash code(deepCopy):" + System.identityHashCode(tileOG));
        System.out.println("Old element hash code(deepCopy):" + System.identityHashCode(tileCopy));

        */
        //-----------------------------------------Prototype------------------------------------------------------

        collisionManager.initializeStaticEntities(level);
    }

    public void addPlayer(Player player) {
        sessionIdToPlayerIndex.put(player.getSessionId(), this.players.size());

        // assign player starting location
        int existingPlayers = this.players.size();
        Vector2 newPlayerLocation = level.getSpawnPoints()[existingPlayers];
        player.setLocationToTile(newPlayerLocation);

        this.players.add(player);
        collisionManager.spatialGrid.addEntity(player, false);
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
            collisionManager.spatialGrid.removeEntity(playerToRemove);
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
        System.out.println("Subscribed to level");
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

        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).execute();
        }

        commands.clear();

        updatePlayersLocations();
        updateBulletsLocations();

        // Detect and handle collisions
        collisionManager.detectCollisions(players, bullets, powerUps);

        // Broadcast updated game state to clients
        broadcastGameState();

    }

    private void updatePlayersLocations() {
        for (Player player : players) {
            player.updateLocation();
            collisionManager.spatialGrid.updateEntity(player);
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
        messagingTemplate.convertAndSend("/server/players", players);
        messagingTemplate.convertAndSend("/server/bullets", bullets);
    }

    @MessageMapping("/command")
    public void processCommand(@Payload Map<String, Object> command, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Integer playerIndex = sessionIdToPlayerIndex.get(sessionId);

        if (playerIndex != null && playerIndex < players.size()) {

            String type = command.get("type").toString();

            switch (type) {
                case "MOVE":
                    MoveCommand moveCommand = new MoveCommand(players.get(playerIndex),
                            ((Integer) command.get("direction")).byteValue());

                    if (!commands.contains(moveCommand)) {
                        commands.add(moveCommand);
                    }
                    break;
                case "FIRE":
                    FireCommand fireCommand = new FireCommand(players.get(playerIndex));
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

    public void spawnPowerUpsAtLocations() {
        PowerUpFactory powerUpFactory = new BasicPowerUpFactory();

        List<Vector2> locations = Arrays.asList(
            new Vector2(2000, 0),
            new Vector2(2000, 9000),
            new Vector2(9000, 2000)
        );

        for (int i = 0; i < locations.size(); i++) {
            Vector2 location = locations.get(i);
            PowerUp powerUp;

            switch (i % 3) {
                case 0:
                    powerUp = powerUpFactory.createHealthPowerUp(location);
                    break;
                case 1:
                    powerUp = powerUpFactory.createSpeedPowerUp(location);
                    break;
                case 2:
                    powerUp = powerUpFactory.createDamagePowerUp(location);
                    break;
                default:
                    powerUp = powerUpFactory.createHealthPowerUp(location);
                }
            addPowerUp(powerUp);
        }
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
        collisionManager.spatialGrid.addEntity(powerUp, false);
    }


}
