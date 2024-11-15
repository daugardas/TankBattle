package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class DamagePowerDown extends PowerDown {
    public DamagePowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        // Reduce player's damage output
        // player.decreaseDamage(5);
        System.out.println("Basic damage power down applied");
    }
}
