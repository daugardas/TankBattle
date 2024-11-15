package com.tankbattle.server.models.items;

import com.tankbattle.server.models.items.advanced.AdvancedDamagePowerDown;
import com.tankbattle.server.models.items.advanced.AdvancedDamagePowerUp;
import com.tankbattle.server.models.items.advanced.AdvancedHealthPowerDown;
import com.tankbattle.server.models.items.advanced.AdvancedHealthPowerUp;
import com.tankbattle.server.models.items.advanced.AdvancedSpeedPowerDown;
import com.tankbattle.server.models.items.advanced.AdvancedSpeedPowerUp;
import com.tankbattle.server.utils.Vector2;

public class AdvancedItemFactory implements ItemFactory {
    @Override
    public PowerUp createHealthPowerUp(Vector2 location) {
        return new AdvancedHealthPowerUp(location, PowerUpType.HEALTH);
    }

    @Override
    public PowerUp createSpeedPowerUp(Vector2 location) {
        return new AdvancedSpeedPowerUp(location, PowerUpType.SPEED);
    }

    @Override
    public PowerUp createDamagePowerUp(Vector2 location) {
        return new AdvancedDamagePowerUp(location, PowerUpType.DAMAGE);
    }

    @Override
    public PowerDown createHealthPowerDown(Vector2 location) {
        return new AdvancedHealthPowerDown(location, PowerDownType.HEALTH);
    }

    @Override
    public PowerDown createSpeedPowerDown(Vector2 location) {
        return new AdvancedSpeedPowerDown(location, PowerDownType.SPEED);
    }

    @Override
    public PowerDown createDamagePowerDown(Vector2 location) {
        return new AdvancedDamagePowerDown(location, PowerDownType.DAMAGE);
    }
}
