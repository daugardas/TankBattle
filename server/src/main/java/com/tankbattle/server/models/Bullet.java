package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class Bullet extends AbstractCollidableEntity {
    private Vector2 location;
    private Vector2 size;

    @JsonIgnore
    private Vector2 direction;
    @JsonIgnore
    private int speed;
    @JsonIgnore
    private int damage;
    @JsonIgnore
    private Player shooter;

    @JsonIgnore
    private boolean markedForRemoval = false;

    public Bullet(Vector2 location, Vector2 direction, Vector2 size, int speed, int damage, Player shooter) {
        this.location = new Vector2();
        this.location.setX(location.getX() + (500 + size.getX() / 2) * direction.getX());
        this.location.setY(location.getY() + (500 + size.getY() / 2) * direction.getY());

        this.direction = new Vector2(direction.getX(), direction.getY());
        this.size = size;
        this.speed = speed;
        this.damage = damage;
        this.shooter = shooter;
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
    public Vector2 getDirection() {
        return direction;
    }

    @JsonIgnore
    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    @JsonIgnore
    public int getDamage() {
        return damage;
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

    @JsonIgnore
    public Player getShooter() {
        return shooter;
    }
}
