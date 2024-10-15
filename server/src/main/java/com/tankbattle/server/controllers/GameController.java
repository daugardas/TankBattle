package com.tankbattle.server.controllers;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.tankbattle.server.builders.LevelBuilder;
import com.tankbattle.server.components.WebSocketSessionManager;
import com.tankbattle.server.factories.DestructibleTileFactory;
import com.tankbattle.server.factories.IndestructibleTileFactory;
import com.tankbattle.server.factories.PassableGroundTileFactory;
import com.tankbattle.server.factories.ProceduralGeneratorFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.strategies.Level.ProceduralGenerator;
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

    private ArrayList<Player> players = new ArrayList();
    private HashMap<String, Integer> sessionIdToPlayerIndex = new HashMap<>();

    private Level level;

    private CollisionManager collisionManager;

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
        collisionManager = new CollisionManager();
        boolean useProceduralGeneration = false;

        if (useProceduralGeneration) {
            ProceduralGenerator generator = ProceduralGeneratorFactory.createGenerator("random");
            LevelBuilder levelBuilder = new LevelBuilder(generator);
            level = levelBuilder.buildLevel(WORLD_WIDTH, WORLD_HEIGHT);
        } else {
            level = buildPredefinedLevel();
        }

        System.out.println("Level initialized. Level:");
        System.out.println(level.toString());
    }

    public void addPlayer(Player player) {
        sessionIdToPlayerIndex.put(player.getSessionId(), this.players.size());
        this.players.add(player);
    }

    public void removePlayerBySessionId(String sessionId) {
        players.removeIf(p -> p.getSessionId().equals(sessionId));
        sessionIdToPlayerIndex.remove(sessionId);

        for (int i = 0; i < players.size(); i++) {
            sessionIdToPlayerIndex.put(players.get(i).getSessionId(), i);
        }
    }

    @SubscribeMapping("/level")
    // send the initial level when client starts listening to /server/level
    public Level sendLevel() {
        System.out.println("Subscribed to level");
        // messagingTemplate.convertAndSend("/server/level", level);

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

    // @Scheduled(fixedRate = 100)
    // public void sendLevelUpdate() {
    // messagingTemplate.convertAndSend("/server/level", level);
    // System.out.println("Sending level update to client");
    // }

    @Scheduled(fixedRate = 33)
    public void sendPlayers() {
        messagingTemplate.convertAndSend("/server/players", players);

        updatePlayersLocations();
    }

    private void updatePlayersLocations() {
        for (Player player : players) {
            player.updateLocation();
        }
    }

    //  private void checkCollisions() {
    //     // Get the level tiles
    //     Tile[][] tiles = level.getGrid();

    //     // Check collisions for each player
    //     for (Player player : players) {
    //         if (collisionManager.checkTileCollision(player, tiles)) {
    //             collisionManager.handlePlayerTileCollision(player);
    //         }
    //     }
    // }

    @MessageMapping("/update-player-movement")
    public void updatePlayer(@Payload byte movementDirection, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        int playerIndex = sessionIdToPlayerIndex.get(sessionId);

        players.get(playerIndex).setMovementDirection(movementDirection);
    }


    //Does not match what I wanted but still works Daugardas will fix it
    public Level buildPredefinedLevel() {
        String mapString = "G G G I I I D D G D\n" +
                           "G G G I G D G G G G\n" +
                           "G G I D G D G D G G\n" +
                           "D G G I D G G I I G\n" +
                           "G G G I G G G I G G\n" +
                           "G G I G G G G G G G\n" +
                           "G I G G G D G I I G\n" +
                           "G D G G I G G G D D\n" +
                           "D G G G G G D I G I\n" +
                           "I G G G G G G D G G";
    
        String[] lines = mapString.split("\n");
        int height = lines.length;
        int width = lines[0].split(" ").length;
    
        Level level = new Level(width, height);
    
        for (int y = 0; y < height; y++) {
            String[] tiles = lines[height - y - 1].split(" ");
            for (int x = 0; x < width; x++) {
                String tileSymbol = tiles[x];
                Tile tile;
                switch(tileSymbol) {
                    case "G":
                        tile = new PassableGroundTileFactory().createTile();
                        break;
                    case "I":
                        tile = new IndestructibleTileFactory().createTile();
                        break;
                    case "D":
                        tile = new DestructibleTileFactory().createTile();
                        break;
                    default:
                        tile = new PassableGroundTileFactory().createTile();
                        break;
                }
                level.setTile(x, y, tile);
            }
        }
    
        Vector2[] spawnPoints = new Vector2[] {
            new Vector2(0, height - 1),
            new Vector2(width - 1, height - 1),
            new Vector2(0, 0),
            new Vector2(width -1, 0)
        };
        level.setSpawnPoints(spawnPoints);
    
        return level;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }
    
}
