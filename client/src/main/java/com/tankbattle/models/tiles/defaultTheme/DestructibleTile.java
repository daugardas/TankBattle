package com.tankbattle.models.tiles.defaultTheme;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class DestructibleTile extends Tile {
    public DestructibleTile() {
        super();
    }

    public DestructibleTile(Vector2 location) {
        super(location);
    }

    public DestructibleTile(int x, int y) {
        super(new Vector2(x, y));
    }

    @Override
    public String toString() {
        return "D";
    }
}
