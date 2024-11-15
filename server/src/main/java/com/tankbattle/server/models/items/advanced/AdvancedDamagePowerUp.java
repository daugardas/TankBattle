package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.items.PowerUp;
import com.tankbattle.server.models.items.PowerUpType;
import com.tankbattle.server.models.tanks.ITank;
import com.tankbattle.server.utils.Vector2;

public class AdvancedDamagePowerUp extends PowerUp {
    public AdvancedDamagePowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(ITank tank) {
        //player.increaseDamage(15); // Advanced damage boost
        System.out.println("Advanced damage power up applied");
    }
}
