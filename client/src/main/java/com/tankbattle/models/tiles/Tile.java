package com.tankbattle.models.tiles;

import com.tankbattle.models.Entity;
import com.tankbattle.utils.Vector2;

import java.awt.image.BufferedImage;

public class Tile extends Entity {
    private final TileType tileType;

    public Tile(TileType type) {
        this.tileType = type;
        this.size = 1000;
    }

    public int getWorldX() {
        return this.location.getX() * 1000;
    }

    public int getWorldY() {
        return this.location.getY() * 1000;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        this.location = new Vector2((float) x, (float) y);
    }

    public BufferedImage getSprite() {
        return this.tileType.getSprite();
    }

    public String getTileTypeName() {
        return this.tileType.getName();
    }

    public String toString() {
        return this.tileType.toString();
    }
}
