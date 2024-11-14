package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.IPlayer;
import com.tankbattle.server.models.PowerUp;
import com.tankbattle.server.models.SpeedBoostDecorator;

@Component
public class PlayerPowerUpCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPowerUpCollisionListener.class);

   private GameController gameController;

    @Autowired
    public PlayerPowerUpCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERUP) {
            return;
        }

        IPlayer player = event.getPlayer();
        PowerUp powerUp = (PowerUp) event.getOtherEntity();

        // Apply power-up effect using Decorator pattern
        switch (powerUp.getType()) {
            case SPEED_BOOST:
                IPlayer decoratedPlayer = new SpeedBoostDecorator(player, 50.0f, 50000L);
                gameController.replacePlayer(player, decoratedPlayer);
                break;
            // Handle other power-up types if needed
            default:
                break;
        }

        gameController.getCollisionManager().spatialGrid.removeEntity(powerUp);
        logger.info("Player {} collected a power-up: {}", player.getUsername(), powerUp.getType());
    }
}
