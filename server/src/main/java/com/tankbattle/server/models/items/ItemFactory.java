package com.tankbattle.server.models.items;

import com.tankbattle.server.utils.Vector2;

public interface ItemFactory {
    PowerUp createHealthPowerUp(Vector2 location);
    PowerUp createSpeedPowerUp(Vector2 location);
    PowerUp createArmorPowerUp(Vector2 location);

    PowerDown createHealthPowerDown(Vector2 location);
    PowerDown createSpeedPowerDown(Vector2 location);
    PowerDown createArmorPowerDown(Vector2 location);
}
