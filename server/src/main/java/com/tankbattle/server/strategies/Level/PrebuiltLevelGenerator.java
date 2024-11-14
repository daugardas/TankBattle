package com.tankbattle.server.strategies.Level;

import com.tankbattle.server.factories.TileFactory;
import com.tankbattle.server.models.Level;
import com.tankbattle.server.models.tiles.Tile;

public class PrebuiltLevelGenerator implements LevelGenerator {
    private String levelString;
    private int width;
    private int height;

    public PrebuiltLevelGenerator() {
        this.levelString = """
                G G G D D D D G G G
                G G G D D D D G G G
                G G G G G G G G G G
                D D D G G G G D D D
                L L L L G G L L L L
                L L L L G G L L L L
                D D D G G G G D D D
                G G G G G G G G G G
                G G G D D D D G G G
                G G G D D D D G G G""";

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

    /**
     * @param width
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param width
     * @param height
     * @param groundFactory
     * @param destructibleFactory
     * @param indestructibleFactory
     * @param liquidFactory
     * @return
     */
    @Override
    public Level generateLevel(int width, int height, TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        String[] lines = levelString.split("\n");
        Level level = new Level(width, height);

        Tile liquid = liquidFactory.createTile();
        Tile indestructible = indestructibleFactory.createTile();
        Tile destructible = destructibleFactory.createTile();
        Tile ground = groundFactory.createTile();

        for (int y = 0; y < height; y++) {
            String[] tiles = lines[height - y - 1].split(" ");
            for (int x = 0; x < width; x++) {
                String tileSymbol = tiles[x];
                Tile tile;

                // Create tile based on symbol, and clone to ensure unique instances
                switch (tileSymbol) {
                    case "L":
                        tile = liquid.copyShallow(); // Ensure liquid tile creation and cloning
                        break;
                    case "I":
                        tile = indestructible.copyShallow(); // Ensure indestructible tile creation and cloning
                        break;
                    case "D":
                        tile = destructible.copyShallow(); // Ensure destructible tile creation and cloning
                        break;
                    default:
                        tile = ground.copyShallow(); // Default to ground tile creation and cloning
                        break;
                }
                level.setTile(x, y, tile); // Set tile in level grid
            }
        }

        return level;
    }
    /**
     * @param groundFactory
     * @param destructibleFactory
     * @param indestructibleFactory
     * @param liquidFactory
     * @return
     */
    @Override
    public Level generateLevel(TileFactory groundFactory, TileFactory destructibleFactory, TileFactory indestructibleFactory, TileFactory liquidFactory) {
        return this.generateLevel(this.width, this.height, groundFactory, destructibleFactory, indestructibleFactory, liquidFactory);
    }
}
