package com.tankbattle.server.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.listeners.CollisionListener;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.PowerUp;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.Vector2;

@Component
public class CollisionManager {
    private final List<CollisionListener> listeners;
    private static final Logger logger = LoggerFactory.getLogger(CollisionManager.class);

    private static final int TILE_WIDTH = GameController.TILE_WIDTH;
    private static final int TILE_HEIGHT = GameController.TILE_HEIGHT;

    public CollisionManager(List<CollisionListener> listeners) {
        this.listeners = listeners;
    }

    public void detectCollisions(List<Player> players, List<Bullet> bullets, List<PowerUp> powerUps, Tile[][] mapTiles) {
        detectPlayerMapCollisions(players, mapTiles);
        detectPlayerPlayerCollisions(players);
        detectPlayerBulletCollisions(players, bullets);
        detectPlayerPowerUpCollisions(players, powerUps);
    }

    private void detectPlayerMapCollisions(List<Player> players, Tile[][] mapTiles) {
        for (Player player : players) {
            if (isCollidingWithMap(player, mapTiles)) {
                notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_MAP, player, null));
            }
        }
    }

    private void detectPlayerPlayerCollisions(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                Player p1 = players.get(i);
                Player p2 = players.get(j);
                if (isColliding(p1, p2)) {
                    notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_PLAYER, p1, p2));
                    notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_PLAYER, p2, p1));
                }
            }
        }
    }

    private void detectPlayerBulletCollisions(List<Player> players, List<Bullet> bullets) {
        for (Player player : players) {
            for (Bullet bullet : bullets) {
                if (isColliding(player, bullet)) {
                    notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_BULLET, player, bullet));
                }
            }
        }
    }

    private void detectPlayerPowerUpCollisions(List<Player> players, List<PowerUp> powerUps) {
        for (Player player : players) {
            for (PowerUp powerUp : powerUps) {
                if (isColliding(player, powerUp)) {
                    notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_POWERUP, player, powerUp));
                }
            }
        }
    }

    private void notifyListeners(CollisionEvent event) {
        for (CollisionListener listener : listeners) {
            listener.onCollision(event);
        }
    }

    // Collision Detection Methods (AABB)
    private boolean isColliding(Player p, Player q) {
        return aabbCollision(p.getLocation(), p.getSize(), q.getLocation(), q.getSize());
    }

    private boolean isColliding(Player p, Bullet b) {
        return aabbCollision(p.getLocation(), p.getSize(), b.getLocation(), b.getSize());
    }

    private boolean isColliding(Player p, PowerUp pu) {
        return aabbCollision(p.getLocation(), p.getSize(), pu.getLocation(), pu.getSize());
    }

    public boolean canMoveTo(Player player, Vector2 newPosition, Tile[][] mapTiles) {
        // Check collision with map at the new position
        return !isCollidingWithMapAtPosition(player, newPosition, mapTiles);
    }

    private boolean isCollidingWithMap(Player player, Tile[][] mapTiles) {
        // Implement map collision logic based on current position
        return isCollidingWithMapAtPosition(player, player.getLocation(), mapTiles);
    }

    private boolean isCollidingWithMapAtPosition(Player player, Vector2 position, Tile[][] mapTiles) {
        // Calculate the bounding box
        float left = position.getX() - player.getSize().getX() / 2;
        float right = position.getX() + player.getSize().getX() / 2;
        float top = position.getY() - player.getSize().getY() / 2;
        float bottom = position.getY() + player.getSize().getY() / 2;
    
        // Determine tile indices
        int leftTile = Math.max(0, (int) Math.floor(left / TILE_WIDTH));
        int rightTile = Math.min(mapTiles[0].length - 1, (int) Math.floor(right / TILE_WIDTH));
        int topTile = Math.max(0, (int) Math.floor(top / TILE_HEIGHT));
        int bottomTile = Math.min(mapTiles.length - 1, (int) Math.floor(bottom / TILE_HEIGHT));
    
        // Iterate through non-passable tiles only
        for (int y = topTile; y <= bottomTile; y++) {
            for (int x = leftTile; x <= rightTile; x++) {
                Tile tile = mapTiles[y][x];
                if (!tile.canPass()) {
                    // Direct AABB check can be optimized or even skipped if tile size aligns with grid
                    if (isAABBOverlapping(position, player.getSize(), new Vector2(x * TILE_WIDTH + TILE_WIDTH / 2, y * TILE_HEIGHT + TILE_HEIGHT / 2), new Vector2(TILE_WIDTH, TILE_HEIGHT))) {
                        logger.info("Collision detected for player '{}' with tile ({}, {})", player.getUsername(), x, y);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isAABBOverlapping(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        // Simplified collision check assuming tiles are axis-aligned and same size
        return Math.abs(pos1.getX() - pos2.getX()) * 2 < (size1.getX() + size2.getX()) &&
               Math.abs(pos1.getY() - pos2.getY()) * 2 < (size1.getY() + size2.getY());
    }
    

    private boolean aabbCollision(Vector2 pos1, Vector2 size1, Vector2 pos2, Vector2 size2) {
        float left1 = pos1.getX() - size1.getX() / 2;
        float right1 = pos1.getX() + size1.getX() / 2;
        float top1 = pos1.getY() - size1.getY() / 2;
        float bottom1 = pos1.getY() + size1.getY() / 2;

        float left2 = pos2.getX() - size2.getX() / 2;
        float right2 = pos2.getX() + size2.getX() / 2;
        float top2 = pos2.getY() - size2.getY() / 2;
        float bottom2 = pos2.getY() + size2.getY() / 2;

        return !(left1 >= right2 || right1 <= left2 || top1 >= bottom2 || bottom1 <= top2);
    }
}
