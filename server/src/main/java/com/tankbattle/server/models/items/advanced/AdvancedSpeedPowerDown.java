package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedSpeedPowerDown extends PowerDown {
    public AdvancedSpeedPowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank player) {
        System.out.println("Advanced speed power down applied");
    }
}
