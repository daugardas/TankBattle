package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedHealthPowerDown extends PowerDown {
    public AdvancedHealthPowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        // Reduce player's health by a larger amount
        // player.decreaseHealth(25);
        System.out.println("Advanced health power down applied");
    }
}
