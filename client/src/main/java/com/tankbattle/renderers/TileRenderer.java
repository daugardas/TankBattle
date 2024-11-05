package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class TileRenderer implements EntityRenderer<Tile>, Scalable {
    private int spriteWidth;
    private int spriteHeight;
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;
    // private long lastFrameTime;
    // private int currentFrame;
    // private static final int FRAME_COUNT = 2;
    // private static final int FRAME_DURATION = 60; // 60ms per frame
    private ResourceManager resourceManager;
    private Map<String, BufferedImage> spriteSheetCache = new HashMap<>();

    public TileRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = 1;
        this.worldLocationScaleFactor = 1;
        // this.currentFrame = 0;
        // this.lastFrameTime = System.currentTimeMillis();
    }

    public TileRenderer(double scaleFactor, ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = scaleFactor;
        this.worldLocationScaleFactor = 1;
        // this.currentFrame = 0;
        // this.lastFrameTime = System.currentTimeMillis();
    }

    private BufferedImage getSpriteSheet(Tile tile) {
        String spriteSheetPath;

        spriteSheetPath = "assets/tiles/" + tile.getClass().getSimpleName() + ".png";

        return spriteSheetCache.computeIfAbsent(spriteSheetPath, path -> resourceManager.loadImage(path));
    }

    private BufferedImage getCurrentFrame(BufferedImage spriteSheet) {
        int y = /* currentFrame */0 * spriteHeight;
        return spriteSheet.getSubimage(0, y, spriteWidth, spriteHeight);
    }

    // private void updateFrame() {
    // long currentTime = System.currentTimeMillis();
    // if (currentTime - lastFrameTime > FRAME_DURATION) {
    // currentFrame = (currentFrame + 1) % FRAME_COUNT;
    // lastFrameTime = currentTime;
    // }
    // }

    @Override
    public void draw(Graphics2D g2d, Tile tile) {
        BufferedImage spriteSheet = getSpriteSheet(tile);
        if (spriteSheet != null && (spriteWidth == 0 || spriteHeight == 0)) {
            spriteWidth = spriteSheet.getWidth();
            spriteHeight = spriteSheet.getHeight() / 1 /* FRAME_COUNT */;
        }
        // updateFrame();
        BufferedImage sprite = getCurrentFrame(spriteSheet);

        double x = Math.floor(tile.getWorldX() * worldLocationScaleFactor + worldOffset.getX()); //helped a bit but still not fixed
        double y = Math.floor(tile.getWorldY() * worldLocationScaleFactor + worldOffset.getY());

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
}
