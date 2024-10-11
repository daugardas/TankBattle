package com.tankbattle.models.tiles.defaultTheme;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class IndestructibleTile extends Tile {
    public IndestructibleTile() {
        super();
    }

    public IndestructibleTile(Vector2 location) {
        super(location);
    }

    public IndestructibleTile(int x, int y) {
        super(new Vector2(x, y));
    }

    @Override
    public String toString() {
        return "I";
    }
}
