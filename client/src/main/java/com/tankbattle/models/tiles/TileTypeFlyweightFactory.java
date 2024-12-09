package com.tankbattle.models.tiles;

import com.tankbattle.controllers.ResourceManager;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TileTypeFlyweightFactory {
    private static final Map<String, TileType> tileTypes = new ConcurrentHashMap<>();
    private final ResourceManager resourceManager;

    public TileTypeFlyweightFactory(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public TileType getTileType(String tileTypeName) {
        return tileTypes.computeIfAbsent(tileTypeName, this::createTileType);
    }

    public TileType createTileType(String tileTypeName) {
        String spriteSheetPath = "assets/tiles/" + tileTypeName + ".png";
        try {
            BufferedImage sprite = resourceManager.loadImage(spriteSheetPath);
            return new TileType(sprite, tileTypeName);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not load tile type " + tileTypeName);
        }
    }

    public static void clearCache(){
        tileTypes.clear();
    }
}
