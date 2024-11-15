package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedArmorPowerDown extends PowerDown {
    public AdvancedArmorPowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        System.out.println("Advanced armor power down applied");
    }
}
