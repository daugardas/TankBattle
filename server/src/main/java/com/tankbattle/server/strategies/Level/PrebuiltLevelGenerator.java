package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public class PrebuiltLevelGenerator extends LevelGenerator {
    private String levelString;

    public PrebuiltLevelGenerator() {
        this.levelString = """
                G D D D I I D D D G
                D D D D I I D D D D
                D D D D I I D D D D
                I D D D D D D D D I
                L L L L D D L L L L
                L L L L D D L L L L
                I D D D D D D D D I
                D D D D I I D D D D
                D D D D I I D D D D
                G D D D I I D D D G""";

        String[] lines = levelString.split("\n");
        this.height = lines.length;
        this.width = lines[0].split(" ").length;
    }

    public PrebuiltLevelGenerator(String levelString) {
        this.levelString = levelString;
        String[] lines = levelString.split("\n");
        this.height = lines.length;
        this.width = lines[0].split(" ").length;
    }

    public void setLevelString(String levelString) {
        this.levelString = levelString;
        String[] lines = levelString.split("\n");
        this.height = lines.length;
        this.width = lines[0].split(" ").length;
    }

    @Override
    protected boolean shouldAddObstacles() {
        return true;
    }

    @Override
    protected void addObstacles(Level level, TileFactory destructibleFactory, TileFactory indestructibleFactory) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String tileSymbol = levelString.split("\n")[height - y - 1].split(" ")[x];
                if (tileSymbol.equals("D")) {
                    level.setTile(x, y, destructibleFactory.createTile());
                } else if (tileSymbol.equals("I")) {
                    level.setTile(x, y, indestructibleFactory.createTile());
                }
            }
        }
    }

    @Override
    protected boolean shouldAddLiquids() {
        if (levelString.contains("L")) {
            return true;
        }
        return false;
    }

    @Override
    protected void addLiquids(Level level, TileFactory liquidFactory) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String tileSymbol = levelString.split("\n")[height - y - 1].split(" ")[x];
                if (tileSymbol.equals("L")) {
                    level.setTile(x, y, liquidFactory.createTile());
                }
            }
        }
    }

    @Override
    protected void addPowerUps(Level level) {
        // todo: implement
    }

    @Override
    protected void addSpawnPoints(Level level) {
        // todo: implement
    }

    
}
