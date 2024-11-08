package com.tankbattle.server.listeners;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PlayerBulletCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerBulletCollisionListener.class);

    private GameController gameController;

    @Autowired
    public PlayerBulletCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_BULLET) {
            return;
        }

        Player player = event.getPlayer();
        Bullet bullet = (Bullet) event.getOtherEntity();

        bullet.markForRemoval();
        player.takeDamage(bullet.getDamage());

        logger.info("Player {} was hit by a bullet. Remaining health: {}", player.getUsername(), player.getHealth());

        // Calculate collision location (e.g., midpoint between the two players)
        int collisionX = (player.getLocation().getX() + bullet.getLocation().getX()) / 2;
        int collisionY = (player.getLocation().getY() + bullet.getLocation().getY()) / 2;

        gameController.removeCollidedBullet(bullet);

        // Notify GameController about the collision location
        gameController.sendCollisionLocation(collisionX, collisionY);
    }
}
