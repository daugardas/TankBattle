package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.models.tanks.Tank;

@Component
public class TankPowerDownCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankPowerDownCollisionListener.class);

    private GameController gameController;

    @Autowired
    public TankPowerDownCollisionListener(@Lazy GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_POWERDOWN) {
            return;
        }

        ITank tank = event.getTank();
        PowerDown powerDown = (PowerDown) event.getOtherEntity();

        gameController.removePowerDown(powerDown);
        powerDown.applyEffect((Tank)tank);

        logger.info("Player collected a power-down.");
    }
}
