package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;

public class PrebuiltLevelGenerator extends LevelGenerator {
    private String levelString;

    public PrebuiltLevelGenerator() {
        this.levelString = """
                G G G G I I G G G G
                D D D D I I D D D D
                D D D D I I D D D D
                I G G G G G G G G I
                L L L L G G L L L L
                L L L L G G L L L L
                I G G G G G G G G I
                D D D D D D D D D D
                D D D D I I D D D D
                G G G G I I G G G G""";

        this.levelString = """
                G G G I I I D D G G
                G G G D G D G G G G
                G G G D G D G G G G
                D G G G D G L I G G
                G G G G L L L I G G
                I G I G L L G G G G
                G I G G G D G I I G
                G D G G G G G G D D
                G G G G D G D G G I
                G G G D I G G G G G""";

        // "GGGGDGDGDG","DDGLLDGLIG","GGGLIGDLGG","LDGDGDGGLG","GGGDGGLDGG","LDGIGGLGLL","GLGGGLGLGD","GLLDLDDGGG","LGDLGGGDLL","GLLGDDGDGG"

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
