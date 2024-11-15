package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.tanks.Tank;

@Component
public class TankMapCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankMapCollisionListener.class);

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() != CollisionEvent.CollisionType.PLAYER_MAP) {
            return;
        }

        Tank tank = event.getTank();
        tank.revertToPreviousPosition();
        // logger.debug("Player {} collided with the map. Reverting position.", player.getUsername());
    }
}
