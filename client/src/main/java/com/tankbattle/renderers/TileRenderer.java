package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class TileRenderer implements EntityRenderer<Tile>, Scalable {
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;
    private long lastFrameTime;
    private int currentFrame;
    private static final int LIQUID_FRAME_COUNT = 2;
    private static final int FRAME_DURATION = 500; // 500ms per frame = 2fps

    public TileRenderer() {
        this.scaleFactor = 1;
        this.worldLocationScaleFactor = 1;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    public TileRenderer(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        this.worldLocationScaleFactor = 1;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    private void updateFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % LIQUID_FRAME_COUNT;
            lastFrameTime = currentTime;
        }
    }

    private BufferedImage getFrame(Tile tile) {
        BufferedImage spriteSheet = tile.getSprite();
        if (tile.getTileTypeName().equals("LiquidTile")) {
            updateFrame();
            
            // Get sprite sheet dimensions
            int spriteWidth = spriteSheet.getWidth();
            int spriteHeight = spriteSheet.getHeight();
            
            // Calculate frame height (total height / number of frames)
            int frameHeight = spriteHeight / LIQUID_FRAME_COUNT;
            
            // Validate dimensions
            if (frameHeight * currentFrame + frameHeight > spriteHeight) {
                System.err.println("Warning: Frame calculation would exceed sprite sheet bounds");
                System.err.println("Sprite dimensions: " + spriteWidth + "x" + spriteHeight);
                System.err.println("Attempted frame: " + currentFrame + " of " + LIQUID_FRAME_COUNT);
                return spriteSheet; // Return full sprite sheet as fallback
            }
            
            try {
                return spriteSheet.getSubimage(
                    0,                          // x position = 0
                    currentFrame * frameHeight, // y position = frame number * frame height
                    spriteWidth,               // use full width
                    frameHeight                // frame height
                );
            } catch (Exception e) {
                System.err.println("Error getting subimage: " + e.getMessage());
                return spriteSheet; // Return full sprite sheet as fallback
            }
        }
        return spriteSheet;
    }

    @Override
    public void draw(Graphics2D g2d, Tile tile) {
        BufferedImage sprite = getFrame(tile);

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
}
