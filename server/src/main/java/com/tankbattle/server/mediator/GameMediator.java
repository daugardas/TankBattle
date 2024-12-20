package com.tankbattle.server.mediator;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.TileEntity;
import com.tankbattle.server.models.items.PowerUp;

public interface GameMediator {
    void handleCollision(CollisionEvent event);
    void removeBullet(Bullet bullet);
    void removePowerUp(PowerUp powerUp);
    void updateTileToGround(TileEntity tile);
    void sendCollisionLocation(int x, int y);
    void awardPoints(Player player, int points, String reason);
} 