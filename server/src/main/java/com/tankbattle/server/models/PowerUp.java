package com.tankbattle.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.utils.Vector2;

public class PowerUp {
    private Vector2 location;
    private Vector2 size;
    private PowerUpType type;

    @JsonIgnore
    private boolean consumed = false;

    public PowerUp(Vector2 location, PowerUpType type) {
        this.location = location;
        this.type = type;
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

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        this.consumed = true;
    }
}
