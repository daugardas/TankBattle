package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedHealthPowerUp extends PowerUp {
    public AdvancedHealthPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        //player.increaseHealth(50); // Advanced health boost
        System.out.println("Advanced health power up applied");
    }
}
