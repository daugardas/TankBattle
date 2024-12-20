package com.tankbattle.server.iterators;

import com.tankbattle.server.models.tiles.Tile;

public class TilePosition {
    private final int x;
    private final int y;
    private final Tile tile;

    public TilePosition(int x, int y, Tile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Tile getTile() { return tile; }
} 