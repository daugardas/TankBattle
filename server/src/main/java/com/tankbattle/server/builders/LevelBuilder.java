package com.tankbattle.server.builders;

import com.tankbattle.server.factories.DestructibleTileFactory;
import com.tankbattle.server.factories.IndestructibleTileFactory;
import com.tankbattle.server.factories.LiquidTileFactory;
import com.tankbattle.server.factories.PassableGroundTileFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.strategies.Level.ProceduralGenerator;

public class LevelBuilder {
    private Level level;
    private ProceduralGenerator generator;

    public LevelBuilder(ProceduralGenerator generator) {
        this.generator = generator;
    }

    public Level buildLevel(int width, int height) {
        return generator.generateLevel(width, height, new PassableGroundTileFactory(), new DestructibleTileFactory(),
                new IndestructibleTileFactory(), new LiquidTileFactory());
    }

    public Level themeLevel(String theme) {
        // TODO: Implement theming
        return level;
    }
}
