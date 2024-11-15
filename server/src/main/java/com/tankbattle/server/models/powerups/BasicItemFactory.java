package com.tankbattle.server.models.powerups;

import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.models.items.basic.DamagePowerDown;
import com.tankbattle.server.models.items.basic.DamagePowerUp;
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
    public PowerUp createDamagePowerUp(Vector2 location) {
        return new DamagePowerUp(location, PowerUpType.DAMAGE);
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
    public PowerDown createDamagePowerDown(Vector2 location) {
        return new DamagePowerDown(location, PowerDownType.DAMAGE);
    }
}
