package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Bullet extends AbstractCollidableEntity implements GameEntity {
    private Vector2 location;
    private Vector2 size;
    @JsonIgnore
    private int damage;
    @JsonIgnore
    private Vector2 velocity;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Vector2 location, Vector2 velocity, int damage) {
        this.location = location;
        this.velocity = velocity;
        this.damage = damage;
        this.size = new Vector2(100, 100);
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

    @JsonIgnore
    public int getDamage() {
        return damage;
    }

    @JsonIgnore
    public Vector2 getVelocity() {
        return velocity;
    }

    @JsonIgnore
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @JsonIgnore
    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    @JsonIgnore
    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    @JsonIgnore
    public void updatePosition() {
        this.location.addToX(velocity.getX());
        this.location.addToY(velocity.getY());
    }
}
