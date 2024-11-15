package com.tankbattle.server.models.items.basic;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpType;
import com.tankbattle.server.models.tanks.Tank;
import com.tankbattle.server.utils.Vector2;

public class DamagePowerUp extends PowerUp {
    public DamagePowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Tank tank) {
        //player.increaseDamage(5); // Basic damage boost
        System.out.println("Basic damage power up applied");
    }
}
