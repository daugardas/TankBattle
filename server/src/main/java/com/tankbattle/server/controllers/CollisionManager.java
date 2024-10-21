package com.tankbattle.server.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.listeners.CollisionListener;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.GameEntity;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.PowerUp;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.SpatialGrid;
import com.tankbattle.server.utils.Vector2;

@Component
public class CollisionManager {
    private final List<CollisionListener> listeners;
    private static final Logger logger = LoggerFactory.getLogger(CollisionManager.class);

    private static final int TILE_WIDTH = GameController.TILE_WIDTH;
    private static final int TILE_HEIGHT = GameController.TILE_HEIGHT;

    // Define cell size 
    private static final int CELL_SIZE = 2000;

    private final SpatialGrid spatialGrid;

    @Autowired
    public CollisionManager(List<CollisionListener> listeners) {
        this.listeners = listeners;
        this.spatialGrid = new SpatialGrid(
            CELL_SIZE,
            GameController.WORLD_WIDTH * TILE_WIDTH,
            GameController.WORLD_HEIGHT * TILE_HEIGHT
        );
    }

    public void detectCollisions(List<Player> players, List<Bullet> bullets, List<PowerUp> powerUps, Tile[][] mapTiles) {
        spatialGrid.clear();

        for (Player player : players) {
            spatialGrid.addEntity(player);
        }
        for (Bullet bullet : bullets) {
            spatialGrid.addEntity(bullet);
        }
        for (PowerUp powerUp : powerUps) {
            spatialGrid.addEntity(powerUp);
        }

        detectPlayerMapCollisions(players, mapTiles);
        detectPlayerPlayerCollisions(players);
        detectPlayerBulletCollisions(players, bullets);
        detectPlayerPowerUpCollisions(players, powerUps);
    }

    private void detectPlayerMapCollisions(List<Player> players, Tile[][] mapTiles) {
        for (Player player : players) {
            if (!canMoveTo(player, player.getLocation(), mapTiles)) {
                notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_MAP, player, null));
            }
        }
    }

    private void detectPlayerPlayerCollisions(List<Player> players) {
        Map<String, Boolean> processedPairs = new HashMap<>();

        for (Player player : players) {
            List<GameEntity> nearbyEntities = spatialGrid.getNearbyEntities(player);
            for (GameEntity entity : nearbyEntities) {
                if (entity instanceof Player && !entity.equals(player)) {
                    Player otherPlayer = (Player) entity;
                    String pairKey = generatePairKey(player, otherPlayer);
                    if (processedPairs.getOrDefault(pairKey, false)) {
                        continue; // Already processed this pair
                    }

                    if (isColliding(player, otherPlayer)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_PLAYER, player, otherPlayer));
                        processedPairs.put(pairKey, true);
                    }
                }
            }
        }
    }

    private void detectPlayerBulletCollisions(List<Player> players, List<Bullet> bullets) {
        for (Player player : players) {
            List<GameEntity> nearbyEntities = spatialGrid.getNearbyEntities(player);
            for (GameEntity entity : nearbyEntities) {
                if (entity instanceof Bullet) {
                    Bullet bullet = (Bullet) entity;
                    if (isColliding(player, bullet)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_BULLET, player, bullet));
                    }
                }
            }
        }
    }

    private void detectPlayerPowerUpCollisions(List<Player> players, List<PowerUp> powerUps) {
        for (Player player : players) {
            List<GameEntity> nearbyEntities = spatialGrid.getNearbyEntities(player);
            for (GameEntity entity : nearbyEntities) {
                if (entity instanceof PowerUp) {
                    PowerUp powerUp = (PowerUp) entity;
                    if (isColliding(player, powerUp)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_POWERUP, player, powerUp));
                    }
                }
            }
        }
    }

    /**
     * Generates a unique key for a pair of players to avoid duplicate collision processing.
     *
     * @param p1 First player.
     * @param p2 Second player.
     * @return A unique string key representing the player pair.
     */
    private String generatePairKey(Player p1, Player p2) {
        return p1.getSessionId().compareTo(p2.getSessionId()) < 0
            ? p1.getSessionId() + "-" + p2.getSessionId()
            : p2.getSessionId() + "-" + p1.getSessionId();
    }

    /**
     * Notifies all registered listeners of a collision event.
     *
     * @param event The collision event to notify listeners about.
     */
    private void notifyListeners(CollisionEvent event) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(event);
        }
    }

    /**
     * Checks if two entities are colliding using Axis-Aligned Bounding Box (AABB) collision detection.
     *
     * @param e1 First entity.
     * @param e2 Second entity.
     * @return True if entities are colliding, else false.
     */
    private boolean isColliding(GameEntity e1, GameEntity e2) {
        Vector2 pos1 = e1.getLocation();
        Vector2 size1 = e1.getSize();
        Vector2 pos2 = e2.getLocation();
        Vector2 size2 = e2.getSize();

        return Math.abs(pos1.getX() - pos2.getX()) * 2 < (size1.getX() + size2.getX()) &&
               Math.abs(pos1.getY() - pos2.getY()) * 2 < (size1.getY() + size2.getY());
    }

    /**
     * Determines if a player can move to the specified position without colliding with the map.
     *
     * @param player      The player attempting to move.
     * @param newPosition The intended new position.
     * @param mapTiles    The game map tiles.
     * @return True if the player can move without collision, else false.
     */
    public boolean canMoveTo(Player player, Vector2 newPosition, Tile[][] mapTiles) {
        // Calculate the bounding box
        float left = newPosition.getX() - player.getSize().getX() / 2;
        float right = newPosition.getX() + player.getSize().getX() / 2;
        float top = newPosition.getY() - player.getSize().getY() / 2;
        float bottom = newPosition.getY() + player.getSize().getY() / 2;

        // Determine tile indices
        int leftTile = Math.max(0, (int) Math.floor(left / TILE_WIDTH));
        int rightTile = Math.min(mapTiles[0].length - 1, (int) Math.floor(right / TILE_WIDTH));
        int topTile = Math.max(0, (int) Math.floor(top / TILE_HEIGHT));
        int bottomTile = Math.min(mapTiles.length - 1, (int) Math.floor(bottom / TILE_HEIGHT));

        // Iterate through non-passable tiles only
        for (int y = topTile; y <= bottomTile; y++) {
            for (int x = leftTile; x <= rightTile; x++) {
                Tile tile = mapTiles[y][x];
                if (tile != null && !tile.canPass()) {
                    // Check AABB overlap between player and tile
                    Vector2 tileCenter = new Vector2(x * TILE_WIDTH + TILE_WIDTH / 2, y * TILE_HEIGHT + TILE_HEIGHT / 2);
                    Vector2 tileSize = new Vector2(TILE_WIDTH, TILE_HEIGHT);
                    if (isAABBOverlapping(newPosition, player.getSize(), tileCenter, tileSize)) {
                        logger.debug("Player '{}' collides with non-passable tile at ({}, {})", player.getUsername(), x, y);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Axis-Aligned Bounding Box (AABB) collision detection between two bounding boxes.
     *
     * @param pos1   Center position of the first bounding box.
     * @param size1  Size of the first bounding box.
     * @param pos2   Center position of the second bounding box.
     * @param size2  Size of the second bounding box.
     * @return True if the bounding boxes overlap, else false.
     */
    private boolean isAABBOverlapping(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        return Math.abs(pos1.getX() - pos2.getX()) * 2 < (size1.getX() + size2.getX()) &&
               Math.abs(pos1.getY() - pos2.getY()) * 2 < (size1.getY() + size2.getY());
    }
}
