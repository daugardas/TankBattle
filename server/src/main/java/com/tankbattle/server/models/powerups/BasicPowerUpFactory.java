package com.tankbattle.server.models.powerups;

import com.tankbattle.server.models.powerups.basic.DamagePowerUp;
import com.tankbattle.server.models.powerups.basic.HealthPowerUp;
import com.tankbattle.server.models.powerups.basic.SpeedPowerUp;
import com.tankbattle.server.utils.Vector2;

public class BasicPowerUpFactory implements PowerUpFactory {
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
}
