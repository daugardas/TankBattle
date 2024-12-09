package com.tankbattle.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.factories.TileFactory;
import com.tankbattle.models.Entity;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.models.tiles.TileTypeFlyweightFactory;
import com.tankbattle.renderers.EntityRenderer;
import com.tankbattle.renderers.Scalable;
import com.tankbattle.renderers.TileRenderer;

public class TileMemoryTest {
    private static final Runtime runtime = Runtime.getRuntime();
    private static final int WORLD_SIZE = 100;
    private static final int ITERATIONS = 100;

    private static final String testLevelString = """
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

    private static long getMemoryUsage() {
        forceGC();

        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static abstract class TileWithoutFlyweight extends Entity {
        // protected int panelX;
        // protected int panelY;

        public TileWithoutFlyweight() {
            this.location = new Vector2();
            this.size = 1000;
        }

        public TileWithoutFlyweight(Vector2 location) {
            this.location = location;
            this.size = 1000;
        }

        public TileWithoutFlyweight(int x, int y) {
            this.location = new Vector2(x, y);
            this.size = 1000;
        }

        public Vector2 getLocation() {
            return location;
        }

        public void setLocation(Vector2 location) {
            this.location = location;
        }

        public void setLocation(int x, int y) {
            this.location = new Vector2(x, y);
        }

        public void setSize(int size) {
            this.size = size;
        }

        public float getWorldX() {
            return location.getX() * 1000;
        }

        public float getWorldY() {
            return location.getY() * 1000;
        }

        public String toString() {
            return this.getClass().getSimpleName();
        }

        public String toShortString() {
            return this.toString().substring(0, 1);
        }
    }

    public static class DestructibleTile extends TileWithoutFlyweight {
        public DestructibleTile() {
            super();
        }

        public DestructibleTile(Vector2 location) {
            super(location);
        }

        public DestructibleTile(int x, int y) {
            super(new Vector2(x, y));
        }

        @Override
        public String toString() {
            return "D";
        }
    }

    public static class IndestructibleTile extends TileWithoutFlyweight {
        public IndestructibleTile() {
            super();
        }

        public IndestructibleTile(Vector2 location) {
            super(location);
        }

        public IndestructibleTile(int x, int y) {
            super(new Vector2(x, y));
        }

        @Override
        public String toString() {
            return "I";
        }
    }

    public static class LiquidTile extends TileWithoutFlyweight {
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

    public static class PassableGroundTile extends TileWithoutFlyweight {
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

    public static class TileFactoryWithoutFlyweight {
        public TileFactoryWithoutFlyweight() {
        }

        public TileWithoutFlyweight createTile(char tileType) throws IllegalArgumentException {
            switch (tileType) {
                case 'D':
                    return new DestructibleTile();
                case 'L':
                    return new LiquidTile();
                case 'I':
                    return new IndestructibleTile();
                case 'G':
                    return new PassableGroundTile();
                default:
                    throw new IllegalArgumentException("Invalid tile type: " + tileType);
            }
        }
    }

    public static class TileRendererWithoutFlyWeight implements EntityRenderer<TileWithoutFlyweight>, Scalable {
        private int spriteWidth;
        private int spriteHeight;
        private double scaleFactor;
        private double worldLocationScaleFactor;
        private Vector2 worldOffset;
        private final ResourceManager resourceManager;
        private final Map<String, BufferedImage> spriteSheetCache = new ConcurrentHashMap<>();

        public TileRendererWithoutFlyWeight(ResourceManager resourceManager) {
            this.resourceManager = resourceManager;
            this.scaleFactor = 1;
            this.worldLocationScaleFactor = 1;
            this.worldOffset = new Vector2(0, 0);
        }

        public TileRendererWithoutFlyWeight(double scaleFactor, ResourceManager resourceManager) {
            this.resourceManager = resourceManager;
            this.scaleFactor = scaleFactor;
            this.worldLocationScaleFactor = 1;
            this.worldOffset = new Vector2(0, 0);
        }

        private BufferedImage getSpriteSheet(TileWithoutFlyweight tile) {
            String spriteSheetPath = "assets/tiles/" + tile.getClass().getSimpleName() + ".png";

            return spriteSheetCache.computeIfAbsent(spriteSheetPath, path -> resourceManager.loadImage(path));
        }

        private BufferedImage getCurrentFrame(BufferedImage spriteSheet) {
            return spriteSheet.getSubimage(0, 0, spriteWidth, spriteHeight);
        }

        @Override
        public void draw(Graphics2D g2d, TileWithoutFlyweight tile) {
            BufferedImage spriteSheet = getSpriteSheet(tile);
            if (spriteSheet != null && (spriteWidth == 0 || spriteHeight == 0)) {
                spriteWidth = spriteSheet.getWidth();
                spriteHeight = spriteSheet.getHeight();
            }

            BufferedImage sprite = getCurrentFrame(spriteSheet);

            double x = tile.getWorldX() * worldLocationScaleFactor + worldOffset.getX();
            double y = tile.getWorldY() * worldLocationScaleFactor + worldOffset.getY();

            AffineTransform oldTransform = g2d.getTransform();
            AffineTransform transform = new AffineTransform();

            transform.translate(x, y);
            transform.scale(scaleFactor, scaleFactor);

            g2d.drawImage(sprite, transform, null);
            g2d.setTransform(oldTransform);
        }

        @Override
        public void setRenderingScaleFactor(double scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        @Override
        public void setWorldLocationScaleFactor(double worldLocationScaleFactor) {
            this.worldLocationScaleFactor = worldLocationScaleFactor;
        }

        @Override
        public void setWorldOffset(Vector2 worldOffset) {
            this.worldOffset = worldOffset;
        }

        public void clearCache() {
            spriteSheetCache.clear();
        }
    }

    private static long testWithoutFlyweight(ResourceManager resourceManager) {
        System.out.println("Testing without Flyweight:");
        long startMemory = getMemoryUsage();

        TileFactoryWithoutFlyweight tileFactory = new TileFactoryWithoutFlyweight();
        TileWithoutFlyweight[][] tiles = new TileWithoutFlyweight[WORLD_SIZE][WORLD_SIZE];
        TileRendererWithoutFlyWeight renderer = new TileRendererWithoutFlyWeight(resourceManager);

        // Create a dummy Graphics2D object to force sprite loading
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummyImage.createGraphics();

        // Parse level string and create tiles
        String[] rows = testLevelString.split("\n");
        int height = rows.length;
        int width = rows[0].trim().split(" ").length;

        for (int y = 0; y < height; y++) {
            String[] tileCodes = rows[y].trim().split(" ");
            for (int x = 0; x < width; x++) {
                char tileType = tileCodes[x].charAt(0);
                tiles[y][x] = tileFactory.createTile(tileType);
                tiles[y][x].setLocation(x, y);
                // Force sprite loading through renderer
                renderer.draw(g2d, (TileWithoutFlyweight) tiles[y][x]);
            }
        }

        // Fill remaining space with ground tiles
        for (int y = height; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                tiles[y][x] = tileFactory.createTile('G');
                tiles[y][x].setLocation(x, y);
                renderer.draw(g2d, (TileWithoutFlyweight) tiles[y][x]);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = width; x < WORLD_SIZE; x++) {
                tiles[y][x] = tileFactory.createTile('G');
                tiles[y][x].setLocation(x, y);
                renderer.draw(g2d, (TileWithoutFlyweight) tiles[y][x]);
            }
        }

        g2d.dispose();

        long endMemory = getMemoryUsage();
        long memoryUsed = (endMemory - startMemory) / 1024;
        System.out.println("Memory used: " + memoryUsed + " KB");
        return memoryUsed;
    }

    private static long testWithFlyweight(ResourceManager resourceManager) {
        System.out.println("Testing with Flyweight:");
        long startMemory = getMemoryUsage();

        TileFactory tileFactory = new TileFactory(resourceManager);
        Tile[][] tiles = new Tile[WORLD_SIZE][WORLD_SIZE];
        TileRenderer renderer = new TileRenderer();

        // Create a dummy Graphics2D object to force sprite loading
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dummyImage.createGraphics();

        // Parse level string and create tiles
        String[] rows = testLevelString.split("\n");
        int height = rows.length;
        int width = rows[0].trim().split(" ").length;

        for (int y = 0; y < height; y++) {
            String[] tileCodes = rows[y].trim().split(" ");
            for (int x = 0; x < width; x++) {
                char tileType = tileCodes[x].charAt(0);
                tiles[y][x] = tileFactory.createTile(tileType);
                tiles[y][x].setLocation(x, y);
                renderer.draw(g2d, tiles[y][x]);
            }
        }
    
        // Fill remaining space with ground tiles
        for (int y = height; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                tiles[y][x] = tileFactory.createTile('G');
                tiles[y][x].setLocation(x, y);
                renderer.draw(g2d, tiles[y][x]);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = width; x < WORLD_SIZE; x++) {
                tiles[y][x] = tileFactory.createTile('G');
                tiles[y][x].setLocation(x, y);
                renderer.draw(g2d, tiles[y][x]);
            }
        }
    
        g2d.dispose();

        long endMemory = getMemoryUsage();
        long memoryUsed = (endMemory - startMemory) / 1024;
        System.out.println("Memory used: " + memoryUsed + " KB");
        return memoryUsed;
    }

    private static void forceGC() {
        runtime.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test() {
        long[] withoutFlyweightMemory = new long[ITERATIONS];
        long[] withFlyweightMemory = new long[ITERATIONS];
        
        for (int i = 0; i < ITERATIONS; i++) {
            System.out.println("\nIteration: " + (i + 1));
            
            TileTypeFlyweightFactory.clearCache();
            forceGC();
            
            ResourceManager resourceManagerWithoutFlyweight = new ResourceManager();
            withoutFlyweightMemory[i] = testWithoutFlyweight(resourceManagerWithoutFlyweight);
            resourceManagerWithoutFlyweight.clearCache();
            
            forceGC();
            
            ResourceManager resourceManager = new ResourceManager();
            withFlyweightMemory[i] = testWithFlyweight(resourceManager);
            resourceManager.clearCache();
        }
        
        // Calculate and print averages
        double avgWithout = Arrays.stream(withoutFlyweightMemory).average().orElse(0);
        double avgWith = Arrays.stream(withFlyweightMemory).average().orElse(0);
        
        System.out.println("\nAverage Statistics:");
        System.out.println("Without Flyweight: " + String.format("%.2f", avgWithout) + " KB");
        System.out.println("With Flyweight: " + String.format("%.2f", avgWith) + " KB");
        System.out.println("Memory Saved: " + String.format("%.2f", avgWithout - avgWith) + " KB");
        System.out.println("Memory Reduction: " + String.format("%.1f", (avgWithout - avgWith) / avgWithout * 100) + "%");
    }
}
