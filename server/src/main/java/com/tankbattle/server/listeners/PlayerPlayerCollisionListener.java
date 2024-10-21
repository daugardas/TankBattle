package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;

@Component
public class PlayerPlayerCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPlayerCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_PLAYER) {
            return;
        }

        Player player1 = event.getPlayer();
        Player player2 = (Player) event.getOtherEntity();

        // Implement collision response, e.g., prevent overlapping
        player1.revertToPreviousPosition();
        player2.revertToPreviousPosition();
        logger.info("Player {} collided with Player {}. Reverting positions.", player1.getUsername(), player2.getUsername());
    }
}
