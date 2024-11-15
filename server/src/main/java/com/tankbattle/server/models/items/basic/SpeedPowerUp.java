package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class SpeedPowerUp extends PowerUp {
    public SpeedPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        //player.increaseSpeed(10); // Basic speed boost
        System.out.println("Basic speed power up applied");
    }
}
