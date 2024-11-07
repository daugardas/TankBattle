package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public interface LevelGenerator {
    void setWidth(int width);

    void setHeight(int height);
    Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory,
            TileFactory indestructibleFactory, TileFactory liquidFactory);

    Level generateLevel(TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory);
}
