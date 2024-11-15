package com.tankbattle.server.models.powerups;

import com.tankbattle.server.models.powerups.advanced.AdvancedDamagePowerUp;
import com.tankbattle.server.models.powerups.advanced.AdvancedHealthPowerUp;
import com.tankbattle.server.models.powerups.advanced.AdvancedSpeedPowerUp;
import com.tankbattle.server.utils.Vector2;

public class AdvancedPowerUpFactory implements PowerUpFactory {
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
}
