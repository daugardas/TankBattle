package com.tankbattle.server.models.items;

import com.tankbattle.server.utils.Vector2;

public interface ItemFactory {
    PowerUp createHealthPowerUp(Vector2 location);
    PowerUp createSpeedPowerUp(Vector2 location);
    PowerUp createDamagePowerUp(Vector2 location);

    PowerDown createHealthPowerDown(Vector2 location);
    PowerDown createSpeedPowerDown(Vector2 location);
    PowerDown createDamagePowerDown(Vector2 location);
}
