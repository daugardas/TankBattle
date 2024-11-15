package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class HealthPowerDown extends PowerDown {
    public HealthPowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        // Reduce player's health by a small amount
        // player.decreaseHealth(10);
        System.out.println("Basic health power down applied");
    }
}
