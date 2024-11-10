package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class TileRenderer implements EntityRenderer<Tile>, Scalable {
    private int spriteWidth;
    private int spriteHeight;
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;
    private ResourceManager resourceManager;
    private Map<String, BufferedImage> spriteSheetCache = new ConcurrentHashMap<>();

    public TileRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = 1;
        this.worldLocationScaleFactor = 1;
    }

    public TileRenderer(double scaleFactor, ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = scaleFactor;
        this.worldLocationScaleFactor = 1;
    }

    private BufferedImage getSpriteSheet(Tile tile) {
        String spriteSheetPath = "assets/tiles/" + tile.getClass().getSimpleName() + ".png";

        return spriteSheetCache.computeIfAbsent(spriteSheetPath, path -> resourceManager.loadImage(path));
    }

    private BufferedImage getCurrentFrame(BufferedImage spriteSheet) {
        return spriteSheet.getSubimage(0, 0, spriteWidth, spriteHeight);
    }

    @Override
    public void draw(Graphics2D g2d, Tile tile) {
        BufferedImage spriteSheet = getSpriteSheet(tile);
        if (spriteSheet != null && (spriteWidth == 0 || spriteHeight == 0)) {
            spriteWidth = spriteSheet.getWidth();
            spriteHeight = spriteSheet.getHeight();
        }
        
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
