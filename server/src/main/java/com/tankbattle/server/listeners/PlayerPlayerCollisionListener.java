package com.tankbattle.server.listeners;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        logger.info("Player {} collided with Player {}. Reverting position.", player1.getUsername(), player2.getUsername());
    }
}
