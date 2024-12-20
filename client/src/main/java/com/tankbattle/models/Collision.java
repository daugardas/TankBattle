// File: com/tankbattle/models/Collision.java
package com.tankbattle.models;

import com.tankbattle.utils.Vector2;
import com.tankbattle.visitors.Visitor;

public class Collision extends Entity {
    private Vector2 location;
    private final long startTime;

    public Collision(Vector2 location) {
        this.location = location;
        this.startTime = System.currentTimeMillis();
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public long getStartTime() {
        return startTime;
    }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
