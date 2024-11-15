package com.tankbattle.server.models.items.advanced;

import com.tankbattle.server.models.Player;
import com.tankbattle.server.models.items.PowerDown;
import com.tankbattle.server.models.items.PowerDownType;
import com.tankbattle.server.utils.Vector2;

public class AdvancedDamagePowerDown extends PowerDown {
    public AdvancedDamagePowerDown(Vector2 location, PowerDownType type) {
        super(location, type);
    }

    @Override
    public void applyEffect(Player player) {
        // Reduce player's damage output significantly
        // player.decreaseDamage(15);
        System.out.println("Advanced damage power down applied");
    }
}
