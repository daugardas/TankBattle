package com.tankbattle.models.tiles;

import java.awt.image.BufferedImage;

public class TileType {
    private final BufferedImage sprite;
    private final String name;

    public TileType(BufferedImage sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    public BufferedImage getSprite() {
        return this.sprite;
    }

    public String getName() {
        return this.name;
    }

    public String toString () {
        return getName().substring(0, 1);
    }
}
