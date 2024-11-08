package com.tankbattle.models;

import com.tankbattle.utils.Vector2;

public class Bullet extends Entity {
    public Bullet() {

    }

    public Bullet(Vector2 location, int size){
        this.location = location;
        this.size = size;
    }

    public Bullet(Vector2 location, Vector2 size) {
        this.location = location;
        this.size = (int)size.getX();
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = (int)size.getX();
    }

    @Override
    public String toString() {
        return "Bullet{" + "location=" + location + ", size=" + size + '}';
    }
}
