package com.tankbattle.models.tanks;

import com.tankbattle.utils.Vector2;

public class Tank {
    private Vector2 size;
    private int health;

    private Vector2 location;
    private Vector2 lookDirection;
    private byte movementDirection;

    public Tank() {
        size = new Vector2(0, 0);
        health = 0;
        location = new Vector2(0, 0);
        lookDirection = new Vector2(0, 0);
        movementDirection = 0;
    }

    public Vector2 getSize() {
        return this.size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Vector2 getLocation() {
        return this.location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getLookDirection() {
        return lookDirection;
    }

    public void setLookDirection(Vector2 lookDirection) {
        this.lookDirection = lookDirection;
    }

    public byte getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(byte movementDirection) {
        this.movementDirection = movementDirection;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "size=" + size +
                ", health=" + health +
                ", location=" + location +
                ", lookDirection=" + lookDirection +
                ", movementDirection=" + movementDirection +
                '}';
    }

}
