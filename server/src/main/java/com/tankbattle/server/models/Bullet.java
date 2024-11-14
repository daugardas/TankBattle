package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Bullet extends AbstractCollidableEntity {
    private Vector2 location;
    private Vector2 size;

    @JsonIgnore
    private Player owner;
    @JsonIgnore
    private Vector2 direction;
    @JsonIgnore
    private int speed = 75;
    @JsonIgnore
    private int damage = 5;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Player owner) {
        this.owner = owner;
        Vector2 ownerLocation = owner.getLocation();

        Vector2 initialBulletLocation = new Vector2(ownerLocation.getX(), ownerLocation.getY());
        Vector2 lookDirection = owner.getLookDirection();

        direction = new Vector2(lookDirection.getX(), lookDirection.getY());
        location = initialBulletLocation;
        location.setX(location.getX() + 500 * direction.getX());
        location.setY(location.getY() + 500 * direction.getY());

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
