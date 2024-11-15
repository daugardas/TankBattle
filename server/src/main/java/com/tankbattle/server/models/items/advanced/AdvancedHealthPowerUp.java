package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedHealthPowerUp extends PowerUp {
    public AdvancedHealthPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        System.out.println("Advanced health power up applied");
    }
}
