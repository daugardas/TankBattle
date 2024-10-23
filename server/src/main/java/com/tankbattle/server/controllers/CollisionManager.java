package com.tankbattle.server.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.listeners.CollisionListener;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.GameEntity;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.PowerUp;
import com.tankbattle.server.models.TileEntity;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.SpatialGrid;
import com.tankbattle.server.utils.Vector2;

@Component
public class CollisionManager {
    private final List<CollisionListener> listeners;
    private static final Logger logger = LoggerFactory.getLogger(CollisionManager.class);

    private static final int TILE_WIDTH = GameController.TILE_WIDTH;
    private static final int TILE_HEIGHT = GameController.TILE_HEIGHT;

    private static final int CELL_SIZE = 1000;

    public SpatialGrid spatialGrid;

    @Autowired
    public CollisionManager(List<CollisionListener> listeners) {
        this.listeners = listeners;
        this.spatialGrid = new SpatialGrid(
            CELL_SIZE,
            GameController.WORLD_WIDTH * TILE_WIDTH,
            GameController.WORLD_HEIGHT * TILE_HEIGHT
        );
    }

    public void initializeStaticEntities(Level level) {
        Tile[][] mapTiles = level.getGrid();
        // Insert map tiles into the spatial grid as static entities
        for (int y = 0; y < mapTiles.length; y++) {
            for (int x = 0; x < mapTiles[0].length; x++) {
                Tile tile = mapTiles[y][x];
                if (tile != null) {
                    TileEntity tileEntity = new TileEntity(tile, x, y);
                    spatialGrid.addEntity(tileEntity, true);
                }
            }
        }
    }
    

    public void detectCollisions(List<Player> players, List<Bullet> bullets, List<PowerUp> powerUps) {
        // Update dynamic entities in the spatial grid
        for (Player player : players) {
            spatialGrid.updateEntity(player);
        }
        for (Bullet bullet : bullets) {
            spatialGrid.updateEntity(bullet);
        }
        for (PowerUp powerUp : powerUps) {
            spatialGrid.updateEntity(powerUp);
        }

        // Detect collisions
        detectEntityCollisions(players, bullets, powerUps);
    }

    // private void detectPlayerMapCollisions(List<Player> players, Tile[][] mapTiles) {
    //     for (Player player : players) {
    //         if (!canMoveTo(player, player.getLocation(), mapTiles)) {
    //             notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_MAP, player, null));
    //         }
    //     }
    // }

    private void detectEntityCollisions(List<Player> players, List<Bullet> bullets, List<PowerUp> powerUps) {
        Set<String> processedPairs = new HashSet<>();

        for (Player player : players) {
            List<GameEntity> nearbyEntities = spatialGrid.getNearbyEntities(player);

            for (GameEntity entity : nearbyEntities) {
                if (entity == player) continue; // Skip self

                if (entity instanceof Player) {
                    Player otherPlayer = (Player) entity;
                    String pairKey = generatePairKey(player, otherPlayer);
                    if (!processedPairs.contains(pairKey) && isColliding(player, otherPlayer)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_PLAYER, player, otherPlayer));
                        processedPairs.add(pairKey);
                    }
                } else if (entity instanceof Bullet) {
                    Bullet bullet = (Bullet) entity;
                    if (isColliding(player, bullet)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_BULLET, player, bullet));
                    }
                } else if (entity instanceof PowerUp) {
                    PowerUp powerUp = (PowerUp) entity;
                    if (isColliding(player, powerUp)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_POWERUP, player, powerUp));
                    }
                } else if (entity instanceof TileEntity) {
                    TileEntity tileEntity = (TileEntity) entity;
                    if (!tileEntity.canPass() && isColliding(player, tileEntity)) {
                        // Handle collision with non-passable tile
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_MAP, player, tileEntity));
                    }
                }
            }
        }
    }

    private String generatePairKey(Player p1, Player p2) {
        return p1.getSessionId().compareTo(p2.getSessionId()) < 0
            ? p1.getSessionId() + "-" + p2.getSessionId()
            : p2.getSessionId() + "-" + p1.getSessionId();
    }

    private void notifyListeners(CollisionEvent event) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(event);
        }
    }

    private boolean isColliding(GameEntity e1, GameEntity e2) {
        Vector2 pos1 = e1.getLocation();
        Vector2 size1 = e1.getSize();
        Vector2 pos2 = e2.getLocation();
        Vector2 size2 = e2.getSize();

        return Math.abs(pos1.getX() - pos2.getX()) * 2 < (size1.getX() + size2.getX()) &&
               Math.abs(pos1.getY() - pos2.getY()) * 2 < (size1.getY() + size2.getY());
    }

    // public boolean canMoveTo(Player player, Vector2 newPosition, Tile[][] mapTiles) {
    //     float left = newPosition.getX() - player.getSize().getX() / 2;
    //     float right = newPosition.getX() + player.getSize().getX() / 2;
    //     float top = newPosition.getY() - player.getSize().getY() / 2;
    //     float bottom = newPosition.getY() + player.getSize().getY() / 2;

    //     int leftTile = Math.max(0, (int) Math.floor(left / TILE_WIDTH));
    //     int rightTile = Math.min(mapTiles[0].length - 1, (int) Math.floor(right / TILE_WIDTH));
    //     int topTile = Math.max(0, (int) Math.floor(top / TILE_HEIGHT));
    //     int bottomTile = Math.min(mapTiles.length - 1, (int) Math.floor(bottom / TILE_HEIGHT));

    //     for (int y = topTile; y <= bottomTile; y++) {
    //         for (int x = leftTile; x <= rightTile; x++) {
    //             Tile tile = mapTiles[y][x];
    //             if (tile != null && !tile.canPass()) {
    //                 Vector2 tileCenter = new Vector2(x * TILE_WIDTH + TILE_WIDTH / 2, y * TILE_HEIGHT + TILE_HEIGHT / 2);
    //                 Vector2 tileSize = new Vector2(TILE_WIDTH, TILE_HEIGHT);
    //                 if (isAABBOverlapping(newPosition, player.getSize(), tileCenter, tileSize)) {
    //                     logger.debug("Player '{}' collides with non-passable tile at ({}, {})", player.getUsername(), x, y);
    //                     return false;
    //                 }
    //             }
    //         }
    //     }
    //     return true;
    // }

    // private boolean isAABBOverlapping(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
    //     return Math.abs(pos1.getX() - pos2.getX()) * 2 < (size1.getX() + size2.getX()) &&
    //            Math.abs(pos1.getY() - pos2.getY()) * 2 < (size1.getY() + size2.getY());
    // }
}
