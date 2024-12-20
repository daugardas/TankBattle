package com.tankbattle.models;

import com.tankbattle.utils.Vector2;
import com.tankbattle.visitors.Visitor;

public class PowerUp extends Entity {
    private Vector2 location;
    private Vector2 size;
    private String type;

    public PowerUp() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

} 