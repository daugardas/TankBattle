package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class SpeedPowerDown extends PowerDown {
    public SpeedPowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        // Reduce player's speed
        // player.decreaseSpeed(1);
        System.out.println("Basic speed power down applied");
    }
}