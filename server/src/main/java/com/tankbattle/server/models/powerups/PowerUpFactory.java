package com.tankbattle.server.models.powerups;

import com.tankbattle.server.utils.Vector2;

public interface PowerUpFactory {
    PowerUp createHealthPowerUp(Vector2 location);
    PowerUp createSpeedPowerUp(Vector2 location);
    PowerUp createDamagePowerUp(Vector2 location);
}
