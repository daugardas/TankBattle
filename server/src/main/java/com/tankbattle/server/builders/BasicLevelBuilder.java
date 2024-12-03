package com.tankbattle.server.builders;

import com.tankbattle.server.factories.DestructibleTileFactory;
import com.tankbattle.server.factories.IndestructibleTileFactory;
import com.tankbattle.server.factories.LiquidTileFactory;
import com.tankbattle.server.factories.PassableGroundTileFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.strategies.Level.LevelGenerator;

public class BasicLevelBuilder extends ILevelBuilder {
    public BasicLevelBuilder(LevelGenerator generator) {
        super(generator);
        System.out.println("Generator: " + generator);
    }

    /**
     * @return
     */
    @Override
    public ILevelBuilder generateLevel() {
        this.level = generator.generateLevel(this.levelWidth, this.levelHeight, new PassableGroundTileFactory(), new DestructibleTileFactory(), new IndestructibleTileFactory(), new LiquidTileFactory());
        return this;
    }

    /**
     * @return
     */
    @Override
    public ILevelBuilder addPowerUps(int count) {
        // todo: implement
        return this;
    }

    /**
     * @return
     */
    @Override
    public Level build() {
        // uztikrinti kad buvo kviestas generate level
        return this.level;
    }
}
