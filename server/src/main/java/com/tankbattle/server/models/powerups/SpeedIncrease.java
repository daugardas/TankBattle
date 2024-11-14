package com.tankbattle.server.models.powerups;

public class SpeedIncrease extends PowerUpDecorator {

    public SpeedIncrease(IPowerUps player) {
        super(player);
    }

    @Override
    public float getSpeed() {
        // Increase speed by 50%
        return player.getSpeed() * 1.5f;
    }
}
