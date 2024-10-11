package com.tankbattle.models.tiles.defaultTheme;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class LiquidTile extends Tile {
    public LiquidTile() {
        super();
    }

    public LiquidTile(Vector2 location) {
        super(location);
    }

    public LiquidTile(int x, int y) {
        super(new Vector2(x, y));
    }

    @Override
    public String toString() {
        return "L";
    }
}
