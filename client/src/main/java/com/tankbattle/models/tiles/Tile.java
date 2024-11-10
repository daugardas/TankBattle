package com.tankbattle.models.tiles;

import com.tankbattle.models.Entity;
import com.tankbattle.utils.Vector2;

public abstract class Tile extends Entity {
    // protected int panelX;
    // protected int panelY;

    public Tile() {
        this.location = new Vector2();
        this.size = 1000;
    }

    public Tile(Vector2 location) {
        this.location = location;
        this.size = 1000;
    }

    public Tile(int x, int y) {
        this.location = new Vector2(x, y);
        this.size = 1000;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        this.location = new Vector2(x, y);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getWorldX() {
        return location.getX() * 1000;
    }

    public float getWorldY() {
        return location.getY() * 1000;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String toShortString() {
        return this.toString().substring(0, 1);
    }
}
