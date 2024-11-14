package com.tankbattle.server.models.powerups;

public class HealthBoost  extends  PowerUpDecorator{

    public HealthBoost(IPowerUps player) {
        super(player);
    }

    @Override
    public int getHealth() {
        return player.getHealth() + 1;
    }
}
