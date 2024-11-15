package com.tankbattle.server.models.powerups.advanced;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpType;
import com.tankbattle.server.utils.Vector2;

public class AdvancedDamagePowerUp extends PowerUp {
    public AdvancedDamagePowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Player player) {
        //player.increaseDamage(15); // Advanced damage boost
        System.out.println("Advanced damage power up applied");
    }
}
