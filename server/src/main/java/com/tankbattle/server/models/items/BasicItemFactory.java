package com.tankbattle.server.models.items;

import com.tankbattle.server.models.items.basic.ArmorPowerDown;
import com.tankbattle.server.models.items.basic.ArmorPowerUp;
import com.tankbattle.server.models.items.basic.HealthPowerDown;
import com.tankbattle.server.models.items.basic.HealthPowerUp;
import com.tankbattle.server.models.items.basic.SpeedPowerDown;
import com.tankbattle.server.models.items.basic.SpeedPowerUp;
import com.tankbattle.server.utils.Vector2;

public class BasicItemFactory implements ItemFactory {
     @Override
    public PowerUp createHealthPowerUp(Vector2 location) {
        return new HealthPowerUp(location, PowerUpType.HEALTH);
    }

    @Override
    public PowerUp createSpeedPowerUp(Vector2 location) {
        return new SpeedPowerUp(location, PowerUpType.SPEED);
    }

    @Override
    public PowerUp createArmorPowerUp(Vector2 location) {
        return new ArmorPowerUp(location, PowerUpType.ARMOR);
    }

    @Override
    public PowerDown createHealthPowerDown(Vector2 location) {
        return new HealthPowerDown(location, PowerDownType.HEALTH);
    }

    @Override
    public PowerDown createSpeedPowerDown(Vector2 location) {
        return new SpeedPowerDown(location, PowerDownType.SPEED);
    }

    @Override
    public PowerDown createArmorPowerDown(Vector2 location) {
        return new ArmorPowerDown(location, PowerDownType.ARMOR);
    }
}
