package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.models.tanks.SpeedBoostDecorator;
import com.tankbattle.server.utils.Vector2;

public class SpeedPowerUp extends PowerUp {
    public SpeedPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        ITank decoratedTank = new SpeedBoostDecorator(tank, 30, 5000);

        GameController gameController = SpringContext.getBean(GameController.class);
        gameController.updateTankReference(tank, decoratedTank);
        System.out.println("Basic speed power up applied");
    }
}
