package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.PowerUp;

@Component
public class PlayerPowerUpCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPowerUpCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERUP) {
            return;
        }

        Player player = event.getPlayer();
        PowerUp powerUp = (PowerUp) event.getOtherEntity();

        // Implement collision response, e.g., apply power-up effect
        //player.applyPowerUp(powerUp.getType());
        powerUp.consume();
        logger.info("Player {} collected a power-up: {}", player.getUsername());//, powerUp.getType());
    }
}
