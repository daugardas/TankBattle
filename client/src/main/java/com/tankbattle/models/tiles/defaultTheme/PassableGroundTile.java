package com.tankbattle.models.tiles.defaultTheme;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class PassableGroundTile extends Tile {
    public PassableGroundTile() {
        super();
    }

    public PassableGroundTile(Vector2 location) {
        super(location);
    }

    public PassableGroundTile(int x, int y) {
        super(new Vector2(x, y));
    }

    @Override
    public String toString() {
        return "G";
    }
}
