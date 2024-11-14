package com.tankbattle.server.models.powerups;

public abstract class PowerUpDecorator implements IPowerUps {
    protected IPowerUps player;

    public PowerUpDecorator(IPowerUps player) {
        this.player = player;
    }

    @Override
    public float getSpeed() {
        return player.getSpeed();
    }

    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public void updateLocation() {
        player.updateLocation();
    }

    @Override
    public void takeDamage(int damage) {
        player.takeDamage(damage);
    }

}
