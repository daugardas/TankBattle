package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;

@Component
public class PlayerMapCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerMapCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_MAP) {
            return;
        }

        Player player = event.getPlayer();
        player.revertToPreviousPosition();
        logger.debug("Player {} collided with the map. Reverting position.", player.getUsername());
    }
}
