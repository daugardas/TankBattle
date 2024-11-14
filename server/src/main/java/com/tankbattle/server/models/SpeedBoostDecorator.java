package com.tankbattle.server.models;

import com.tankbattle.server.utils.Vector2;

public class SpeedBoostDecorator extends PlayerDecorator {
    private float speedBoost;
    private long duration;
    private long startTime;

    public SpeedBoostDecorator(IPlayer player, float speedBoost, long duration) {
        super(player);
        this.speedBoost = speedBoost;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateLocation() {
        if (System.currentTimeMillis() - startTime < duration) {
            if (decoratedPlayer instanceof Player) {
                Player player = (Player) decoratedPlayer;
                float originalSpeed = player.getSpeed();
                player.setSpeed(originalSpeed + speedBoost);
                player.updateLocation();
                player.setSpeed(originalSpeed);
            } else {
                decoratedPlayer.updateLocation();
            }
        } else {
            removeDecorator();
            decoratedPlayer.updateLocation();
        }
    }

    private void removeDecorator() {
        if (decoratedPlayer instanceof PlayerDecorator) {
            decoratedPlayer = ((PlayerDecorator) decoratedPlayer).decoratedPlayer;
        }
    }

    @Override
    public void takeDamage(int damage) {
        decoratedPlayer.takeDamage(damage);
    }

    @Override
    public int getHealth() {
        return decoratedPlayer.getHealth();
    }

    @Override
    public void setHealth(int health) {
        decoratedPlayer.setHealth(health);
    }

    @Override
    public void setLocation(Vector2 location) {
        decoratedPlayer.setLocation(location);
    }

    @Override
    public void setSize(Vector2 size) {
        decoratedPlayer.setSize(size);
    }

    @Override
    public double getRotationAngle() {
        return decoratedPlayer.getRotationAngle();
    }

    @Override
    public void updateRotationAngle() {
        decoratedPlayer.updateRotationAngle();
    }

    @Override
    public String getSessionId() {
        return decoratedPlayer.getSessionId();
    }

    @Override
    public String getUsername() {
        return decoratedPlayer.getUsername();
    }

    @Override
    public void revertToPreviousPosition() {
        decoratedPlayer.revertToPreviousPosition();
    }

    @Override
    public Vector2 getLocation() {
        return decoratedPlayer.getLocation();
    }

    @Override
    public Vector2 getSize() {
        return decoratedPlayer.getSize();
    }

    @Override
    public Player getPlayer() {
        return decoratedPlayer.getPlayer();
    }
}

