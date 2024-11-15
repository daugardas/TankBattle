package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedArmorPowerUp extends PowerUp {
    public AdvancedArmorPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        System.out.println("Advanced armor power up applied");
    }
}
