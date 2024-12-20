package com.tankbattle.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tankbattle.server.events.CollisionEvent;
import com.tankbattle.server.mediator.GameMediator;

@Component
public class TankPowerUpCollisionListener implements CollisionListener {
    private static final Logger logger = LoggerFactory.getLogger(TankPowerUpCollisionListener.class);

    private final GameMediator gameMediator;

    @Autowired
    public TankPowerUpCollisionListener(@Lazy GameMediator gameMediator) {
        this.gameMediator = gameMediator;
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (event.getType() == CollisionEvent.CollisionType.PLAYER_POWERUP) {
            gameMediator.handleCollision(event);
        }
    }
}
