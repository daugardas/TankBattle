package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;

@Component
public class PlayerBulletCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerBulletCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_BULLET) {
            return;
        }

        Player player = event.getPlayer();
        Bullet bullet = (Bullet) event.getOtherEntity();

        // Implement collision response, e.g., apply damage
        //player.applyDamage(bullet.getDamage());
        bullet.markForRemoval();
        logger.info("Player {} was hit by a bullet. Remaining health:", player.getUsername()); //player.getHealth());
    }
}
