package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public interface ProceduralGenerator {
    Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory,
            TileFactory indestructibleFactory, TileFactory liquidFactory);
}
