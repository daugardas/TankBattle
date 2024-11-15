package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.tanks.ITank;

@Component
public class TankPowerUpCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankPowerUpCollisionListener.class);

    private GameController gameController;

    @Autowired
    public TankPowerUpCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERUP) {
            return;
        }

        ITank tank = event.getTank();
        PowerUp powerUp = (PowerUp) event.getOtherEntity();

        gameController.removePowerUp(powerUp);
        powerUp.applyEffect(tank);
        logger.info("Player collected a power-up");
    }
}
