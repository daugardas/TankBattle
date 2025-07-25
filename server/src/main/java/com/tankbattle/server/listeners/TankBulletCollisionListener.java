package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tanks.ITank;

@Component
public class TankBulletCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankBulletCollisionListener.class);

    private GameController gameController;

    @Autowired
    public TankBulletCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_BULLET) {
            return;
        }

        ITank tank = event.getTank();
        Bullet bullet = (Bullet) event.getOtherEntity();
        Player shooter = bullet.getShooter();

        bullet.markForRemoval();
        tank.takeDamage(bullet.getDamage());

        // Award points to shooter if they hit someone else
        if (shooter != null && tank != shooter.getTank()) {
            shooter.addScore(10); // 10 points for hitting another player
            logger.info("Player '{}' scored hit on enemy tank (+10 points)", shooter.getUsername());
        }

        logger.info("Player was hit by a bullet. Remaining health: {}", tank.getHealth());

        // Calculate collision location (e.g., midpoint between the two players)
        int collisionX = (tank.getLocation().getX() + bullet.getLocation().getX()) / 2;
        int collisionY = (tank.getLocation().getY() + bullet.getLocation().getY()) / 2;

        gameController.removeCollidedBullet(bullet);

        // Notify GameController about the collision location
        gameController.sendCollisionLocation(collisionX, collisionY);
    }
}
