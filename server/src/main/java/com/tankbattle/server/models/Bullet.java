package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Bullet extends AbstractCollidableEntity implements GameEntity {
    private Vector2 location;
    private Vector2 size;
    @JsonIgnore
    private Vector2 direction;
    @JsonIgnore
    private int speed = 75;
    @JsonIgnore
    private int damage = 5;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Vector2 location, Vector2 direction) {
        this.direction = new Vector2(direction.getX(), direction.getY());

        Vector2 initialLocation = new Vector2(location.getX(), location.getY());
        Vector2 offset = new Vector2(direction.getX(), direction.getY());
        offset.multiply(800);
        this.location = initialLocation.addVector(offset);

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
    public Vector2 getDirection() {
        return direction;
    }

    @JsonIgnore
    public void setDirection(Vector2 direction) {
        this.direction = direction;
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
        this.location.addToX(direction.getX() * speed);
        this.location.addToY(direction.getY() * speed);
    }

    @JsonIgnore
    public String toString() {
        return String.format("%s %s", location, direction);
    }
}
