package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class HealthPowerUp extends PowerUp {
    public HealthPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        //player.increaseHealth(20); // Basic health boost
        System.out.println("Basic health power up applied");
    }
}
