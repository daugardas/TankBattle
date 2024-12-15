package com.tankbattle.server.listeners;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.TileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class BulletMapCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(BulletMapCollisionListener.class);

    private final GameController gameController;

    @Autowired
    public BulletMapCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.BULLET_MAP) {
            return;
        }

        Bullet bullet = (Bullet) event.getFirstEntity();
        TileEntity tile = (TileEntity) event.getOtherEntity();

        tile.takeDamage(bullet.getDamage());
        if (tile.getHealth() <= 0) {
            GameController gameController = SpringContext.getBean(GameController.class);
            gameController.updateLevelTileToGround(tile);
        }

        // Calculate collision location (e.g., midpoint between the two players)
        int collisionX = (bullet.getLocation().getX() + tile.getLocation().getX()) / 2;
        int collisionY = (bullet.getLocation().getY() + tile.getLocation().getY()) / 2;

        gameController.removeCollidedBullet(bullet);

        // Notify GameController about the collision location
        gameController.sendCollisionLocation(collisionX, collisionY);
    }
}
