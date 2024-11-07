package com.tankbattle.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tankbattle.server.builders.BasicLevelBuilder;
import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.factories.LevelGeneratorFactory;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.PowerUp;
import com.tankbattle.server.strategies.Level.LevelGenerator;
import com.tankbattle.server.utils.Vector2;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.*;

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
    private List<Bullet> bullets = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

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
            // Remove the player from the spatial grid
             System.out.println("Removing player from grid cells. MinIndices: " + 
                       Arrays.toString(playerToRemove.getCellIndicesMin()) + 
                       ", MaxIndices: " + Arrays.toString(playerToRemove.getCellIndicesMax()));
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

        // workaround for sending the hard-coded map, as the generation sometimes crashes
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
        updatePlayersLocations();

        // Detect and handle collisions
        collisionManager.detectCollisions(players, bullets, powerUps);

        // Broadcast updated game state to clients
        broadcastGameState();
    }

    private void updatePlayersLocations() {
        for (Player player : players) {
            player.updateLocation();
        }
    }

    private void broadcastGameState() {
        messagingTemplate.convertAndSend("/server/players", players);
    }

    @MessageMapping("/update-player-movement")
    public void updatePlayer(@Payload byte movementDirection, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        Integer playerIndex = sessionIdToPlayerIndex.get(sessionId);
        if (playerIndex != null && playerIndex < players.size()) {
            players.get(playerIndex).setMovementDirection(movementDirection);
        } else {
            System.err.println("Invalid session ID or player index: " + sessionId);
        }
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void sendCollisionLocation(int x, int y) {
        Map<String, Integer> collisionLocation = new HashMap<>();
        collisionLocation.put("x", x);
        collisionLocation.put("y", y);

        messagingTemplate.convertAndSend("/server/collisions", collisionLocation);

        System.out.println("Sent collision location: x=" + x + ", y=" + y);
    }
}
