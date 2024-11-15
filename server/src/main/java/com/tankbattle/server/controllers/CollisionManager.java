package com.tankbattle.server.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.listeners.CollisionListener;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.GameEntity;
import com.tankbattle.server.models.ICollidableEntity;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.TileEntity;
import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.SpatialGrid;
import com.tankbattle.server.utils.Vector2;

@Component
public class CollisionManager {
    private final List<CollisionListener> listeners;

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
                GameController.WORLD_HEIGHT * TILE_HEIGHT);
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

    //updating may be done after update player, bullet, powerup
    public void detectCollisions(List<ITank> tanks, List<Bullet> bullets, List<PowerUp> powerUps, List<PowerDown> powerDowns) {
        for (PowerUp powerUp : powerUps) {
            spatialGrid.updateEntity(powerUp);
        }


        for (PowerDown powerDown : powerDowns) {
            spatialGrid.updateEntity(powerDown);
        }

        detectEntityCollisions(tanks, bullets, powerUps, powerDowns);
    }

    private void detectEntityCollisions(List<ITank> tanks, List<Bullet> bullets, List<PowerUp> powerUps, List<PowerDown> powerDowns) {
        Set<String> processedPairs = new HashSet<>();

        for (Bullet bullet : bullets) {
            List<ICollidableEntity> nearbyEntities = spatialGrid.getNearbyEntities(bullet);

            for (ICollidableEntity gameEntity : nearbyEntities) {
                if (gameEntity instanceof TileEntity tileEntity) {
                    if (!tileEntity.canProjectilePass() && isColliding(bullet, tileEntity)) {
                        notifyListeners(
                                new CollisionEvent(CollisionEvent.CollisionType.BULLET_MAP, bullet, tileEntity));
                    }
                }
            }
        }

        // if bullet can hit two tiles simultaneously, we can just mark it for removal,
        // and when checking collisions, check if it's marked.
        // If it is marked, we just not send the collision event.
        // After checking every bullet for collisions, we can remove collided bullets

        for (ITank tank : tanks) {
            List<ICollidableEntity> nearbyEntities = spatialGrid.getNearbyEntities(tank);

            for (ICollidableEntity entity : nearbyEntities) {
                if (entity == tank)
                    continue;

                // switch
                if (entity instanceof ITank otherTank) {
                    notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_PLAYER, tank, otherTank));
                } else if (entity instanceof Bullet bullet) {
                    if (isColliding(tank, bullet)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_BULLET, tank, bullet));
                    }
                } else if (entity instanceof PowerUp powerUp) {
                    if (isColliding(tank, powerUp)) {
                        notifyListeners(
                                new CollisionEvent(CollisionEvent.CollisionType.PLAYER_POWERUP, tank, powerUp));
                    }
                } else if (entity instanceof PowerDown powerDown) {
                    if (isColliding(tank, powerDown)) {
                        notifyListeners(new CollisionEvent(CollisionEvent.CollisionType.PLAYER_POWERDOWN, tank, powerDown));
                    }
                } else if (entity instanceof TileEntity tileEntity) {
                    if (!tileEntity.canPass() && isColliding(tank, tileEntity)) {
                        notifyListeners(
                                new CollisionEvent(CollisionEvent.CollisionType.PLAYER_MAP, tank, tileEntity));
                    }
                }
            }
        }

    }

    // not ideal
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
}
