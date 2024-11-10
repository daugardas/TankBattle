package com.tankbattle.renderers;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.Collision;
import com.tankbattle.utils.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExplosionRenderer implements EntityRenderer<Collision>, Scalable {
    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final int FRAME_COUNT = 5;
    private static final int FRAME_DURATION = 100;
    private final ResourceManager resourceManager;
    private double scaleFactor = 1.0;
    private double worldLocationScaleFactor = 1.0;
    private Vector2 worldOffset = new Vector2(0, 0);
    private BufferedImage[] explosionFrames;

    public ExplosionRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        loadSpriteSheetAndFrames();
    }

    private void loadSpriteSheetAndFrames() {
        String spriteSheetPath = "assets/images/Explosion.png";
        BufferedImage spriteSheet = resourceManager.loadImage(spriteSheetPath);

        if (spriteSheet == null) {
            System.err.println("Explosion sprite sheet could not be loaded.");
            return;
        }

        if (spriteSheet.getHeight() < FRAME_COUNT * FRAME_HEIGHT || spriteSheet.getWidth() < FRAME_WIDTH) {
            throw new IllegalArgumentException("The sprite sheet does not have enough frames or dimensions are incorrect.");
        }

        explosionFrames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            int offsetY = i * FRAME_HEIGHT;
            explosionFrames[i] = spriteSheet.getSubimage(0, offsetY, FRAME_WIDTH, FRAME_HEIGHT);
        }
    }

    @Override
    public void draw(Graphics2D g2d, Collision collision) {
        if (explosionFrames == null || explosionFrames.length == 0) {
            return;
        }
        long elapsedTime = System.currentTimeMillis() - collision.getTimestamp();
        int currentFrame = (int) (elapsedTime / FRAME_DURATION);
        if (currentFrame >= FRAME_COUNT) {
            return;
        }
        BufferedImage currentSprite = explosionFrames[currentFrame];
        Vector2 location = collision.getLocation();
        double x = location.getX() * worldLocationScaleFactor + worldOffset.getX();
        double y = location.getY() * worldLocationScaleFactor + worldOffset.getY();
        g2d.drawImage(currentSprite, (int) (x - FRAME_WIDTH * scaleFactor / 2), (int) (y - FRAME_HEIGHT * scaleFactor / 2), (int) (FRAME_WIDTH * scaleFactor), (int) (FRAME_HEIGHT * scaleFactor), null);
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
