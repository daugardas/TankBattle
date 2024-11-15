package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.powerups.PowerUp;
import com.tankbattle.server.models.powerups.PowerUpType;
import com.tankbattle.server.utils.Vector2;

public class AdvancedSpeedPowerUp extends PowerUp {
    public AdvancedSpeedPowerUp(Vector2 location, PowerUpType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Player player) {
        //player.increaseSpeed(25); // Advanced speed boost
        System.out.println("Advanced speed power up applied");
    }
}
