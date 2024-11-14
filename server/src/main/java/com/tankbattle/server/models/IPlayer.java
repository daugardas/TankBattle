package com.tankbattle.server.models;

import com.tankbattle.server.utils.Vector2;

public interface IPlayer extends GameEntity {
    // Movement methods
    public void updateLocation();
    public void setMovementDirection(byte movementDirection);
    public byte getMovementDirection();

    // Health methods
    public void takeDamage(int damage);
    public int getHealth();
    public void setHealth(int health);

    // Position and size methods
    public void setLocation(Vector2 location);
    public void setSize(Vector2 size);

    // Other methods
    public double getRotationAngle();
    public void updateRotationAngle();
    public String getSessionId();
    public String getUsername();
    public void revertToPreviousPosition();
}
