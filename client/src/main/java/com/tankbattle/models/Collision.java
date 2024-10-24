// File: com/tankbattle/models/Collision.java
package com.tankbattle.models;

import com.tankbattle.utils.Vector2;

public class Collision extends Entity {
    private final Vector2 location;
    private final long timestamp;

    public Collision(Vector2 location) {
        this.location = location;
        this.timestamp = System.currentTimeMillis();
    }

    public Vector2 getLocation() {
        return location;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
