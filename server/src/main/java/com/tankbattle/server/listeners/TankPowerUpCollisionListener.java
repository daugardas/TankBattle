package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.tanks.Tank;

@Component
public class TankPowerUpCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankPowerUpCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERUP) {
            return;
        }

        Tank tank = event.getTank();
        PowerUp powerUp = (PowerUp) event.getOtherEntity();

        powerUp.applyEffect(tank);

        logger.info("Player collected a power-up");
    }
}
