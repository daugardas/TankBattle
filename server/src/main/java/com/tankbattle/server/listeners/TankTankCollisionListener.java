package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tanks.Tank;

@Component
public class TankTankCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankTankCollisionListener.class);

    private GameController gameController;

    @Autowired
    public TankTankCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_PLAYER) {
            return;
        }

        Tank tank1 = event.getTank();
        Tank tank2 = (Tank) event.getOtherEntity();

        // Implement collision response, e.g., prevent overlapping
        tank1.revertToPreviousPosition();
        tank2.revertToPreviousPosition();
        // logger.info("Player {} collided with Player {}. Reverting positions.", player1.getUsername(), player2.getUsername());

        // Calculate collision location (e.g., midpoint between the two players)
        int collisionX = (tank1.getLocation().getX() + tank2.getLocation().getX()) / 2;
        int collisionY = (tank1.getLocation().getY() + tank2.getLocation().getY()) / 2;

        // Notify GameController about the collision location
        gameController.sendCollisionLocation(collisionX, collisionY);
    }
}
