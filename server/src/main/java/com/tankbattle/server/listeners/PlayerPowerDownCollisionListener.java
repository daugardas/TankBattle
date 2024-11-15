package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.items.PowerDown;

@Component
public class PlayerPowerDownCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPowerDownCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERDOWN) {
            return;
        }

        Player player = event.getPlayer();
        PowerDown powerDown = (PowerDown) event.getOtherEntity();

        powerDown.applyEffect(player);

        logger.info("Player {} collected a power-down.", player.getUsername());
    }
}
