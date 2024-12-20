package com.tankbattle.models;

import com.tankbattle.utils.Vector2;
import com.tankbattle.visitors.Visitor;

public class Bullet extends Entity {
    private Vector2 size;

    public Bullet() {

    }

    public Bullet(Vector2 location, Vector2 size) {
        this.location = location;
        this.size = size;
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

    @Override
    public String toString() {
        return "Bullet{" + "location=" + location + ", size=" + size + '}';
    }
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
