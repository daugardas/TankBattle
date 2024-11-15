package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.HealthBoostDecorator;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class HealthPowerUp extends PowerUp {
    public HealthPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        ITank decoratedTank = new HealthBoostDecorator(tank, 50, 5000);
        GameController gameController = SpringContext.getBean(GameController.class);
        gameController.updateTankReference(tank, decoratedTank);
        System.out.println("Health boost power-up applied.");
    }
}
