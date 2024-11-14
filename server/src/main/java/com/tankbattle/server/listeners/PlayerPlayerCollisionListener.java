package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.IPlayer;

@Component
public class PlayerPlayerCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPlayerCollisionListener.class);

    private GameController gameController;

    @Autowired
    public PlayerPlayerCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_PLAYER) {
            return;
        }

        IPlayer player1 = event.getPlayer();
        IPlayer player2 = (IPlayer) event.getOtherEntity();

        // Implement collision response, e.g., prevent overlapping
        player1.revertToPreviousPosition();
        player2.revertToPreviousPosition();
        logger.info("Player {} collided with Player {}. Reverting positions.", player1.getUsername(), player2.getUsername());

        // Calculate collision location (e.g., midpoint between the two players)
        int collisionX = (player1.getLocation().getX() + player2.getLocation().getX()) / 2;
        int collisionY = (player1.getLocation().getY() + player2.getLocation().getY()) / 2;

        // Notify GameController about the collision location
        gameController.sendCollisionLocation(collisionX, collisionY);
    }
}
