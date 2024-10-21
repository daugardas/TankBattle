package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Bullet {
    private Vector2 location;
    private Vector2 size;
    private int damage;
    private Vector2 velocity;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Vector2 location, Vector2 velocity, int damage) {
        this.location = location;
        this.velocity = velocity;
        this.damage = damage;
        this.size = new Vector2(100, 100); // Example size
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public int getDamage() {
        return damage;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public void updatePosition() {
        this.location.addToX(velocity.getX());
        this.location.addToY(velocity.getY());
    }
}
