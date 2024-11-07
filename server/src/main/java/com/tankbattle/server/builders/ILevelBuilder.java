package com.tankbattle.server.builders;

import com.tankbattle.server.models.Level;
import com.tankbattle.server.strategies.Level.LevelGenerator;
import com.tankbattle.server.utils.Vector2;

public abstract class ILevelBuilder {
    protected Level level;
    protected LevelGenerator generator;
    protected int levelWidth = 10;
    protected int levelHeight = 10;

    public ILevelBuilder(LevelGenerator generator) {
        this.generator = generator;
    }

    public void setGenerator(LevelGenerator generator) {
        this.generator = generator;
    }

    public abstract ILevelBuilder generateLevel();

    public ILevelBuilder setWidth(int width) {
        this.levelWidth = width;
        return this;
    }

    public ILevelBuilder setHeight(int height) {
        this.levelHeight = height;
        return this;
    }

    public ILevelBuilder addSpawnPoints(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Spawn point count must be greater than 0");
        }

        if (count > 4) {
            throw new IllegalArgumentException("Spawn point count must be less than 4");
        }

        this.level.setSpawnPointsCount(count);
        Vector2[] spawnPoints = new Vector2[level.getSpawnPointsCount()];

        switch (count) {
            case 1:
                spawnPoints[0] = new Vector2(0, 0);
                break;
            case 2:
                spawnPoints[0] = new Vector2(0, 0);
                spawnPoints[1] = new Vector2(this.level.getWidth() - 1, 0);
                break;
            case 3:
                spawnPoints[0] = new Vector2(0, 0);
                spawnPoints[1] = new Vector2(this.level.getWidth() - 1, 0);
                spawnPoints[2] = new Vector2(0, this.level.getHeight() - 1);
                break;
            case 4:
                spawnPoints[0] = new Vector2(0, 0);
                spawnPoints[1] = new Vector2(this.level.getWidth() - 1, 0);
                spawnPoints[2] = new Vector2(0, this.level.getHeight() - 1);
                spawnPoints[3] = new Vector2(this.level.getWidth() - 1, this.level.getHeight() - 1);
        }

        for (int i = 0; i < this.level.getSpawnPointsCount(); i++) {
            this.level.setSpawnLocation(i, spawnPoints[i].getX(), spawnPoints[i].getY());
        }

        return this;
    }

    public abstract ILevelBuilder addPowerUps(int count);

    public abstract Level build();

    protected ILevelBuilder reset() {
        this.level = null;
        this.generator = null;
        this.levelWidth = 10;
        this.levelHeight = 10;

        return this;
    }
}
