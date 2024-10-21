// File: com/tankbattle/renderers/ExplosionRenderer.java
package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.Collision;
import com.tankbattle.utils.Vector2;

public class ExplosionRenderer implements EntityRenderer<Collision>, Scalable {
    private double scaleFactor = 1.0;
    private double worldLocationScaleFactor = 1.0;
    private Vector2 worldOffset = new Vector2(0, 0);

    private BufferedImage explosionSpriteSheet;
    private static final int FRAME_WIDTH = 32; // Each frame is 32px wide
    private static final int FRAME_HEIGHT = 32; // Each frame is 32px tall
    private static final int FRAME_COUNT = 5;  // 5 frames in one column
    private static final int FRAME_DURATION = 100; // Duration per frame in milliseconds

    private ResourceManager resourceManager;

    public ExplosionRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        loadSpriteSheet();
    }

    // Load the explosion sprite sheet using ResourceManager
    private void loadSpriteSheet() {
        String spriteSheetPath = "/com/tankbattle/assets/images/Explotion.png"; // Adjust the path as needed
        explosionSpriteSheet = resourceManager.loadImage(spriteSheetPath);

        if (explosionSpriteSheet == null) {
            System.err.println("Explosion sprite sheet could not be loaded.");
        } else if (explosionSpriteSheet.getHeight() < FRAME_COUNT * FRAME_HEIGHT || explosionSpriteSheet.getWidth() < FRAME_WIDTH) {
            throw new IllegalArgumentException("The sprite sheet does not have enough frames or dimensions are incorrect.");
        }
    }

    @Override
    public void draw(Graphics2D g2d, Collision collision) {
        if (explosionSpriteSheet == null) {
            return; // If the sprite sheet couldn't be loaded, skip drawing
        }

        // Determine the frame to render based on time since the collision occurred
        long elapsedTime = System.currentTimeMillis() - collision.getTimestamp();
        int currentFrame = (int) (elapsedTime / FRAME_DURATION);

        if (currentFrame >= FRAME_COUNT) {
            currentFrame = FRAME_COUNT - 1; // Keep showing the last frame if animation is done
        }

        Vector2 location = collision.getLocation();
        double x = location.getX() * worldLocationScaleFactor + worldOffset.getX();
        double y = location.getY() * worldLocationScaleFactor + worldOffset.getY();

        // Calculate the Y offset for the current frame in the sprite sheet
        int offsetY = currentFrame * FRAME_HEIGHT;

        // Ensure the subimage extraction does not exceed the sprite sheet bounds
        if (offsetY + FRAME_HEIGHT > explosionSpriteSheet.getHeight() || FRAME_WIDTH > explosionSpriteSheet.getWidth()) {
            System.err.println("Frame extraction exceeds sprite sheet bounds.");
            return;
        }

        // Get the current frame from the sprite sheet (fixed width and height of 32x32)
        BufferedImage currentSprite = explosionSpriteSheet.getSubimage(0, offsetY, FRAME_WIDTH, FRAME_HEIGHT);

        // Draw the explosion at the collision location, with the sprite centered at (x, y)
        g2d.drawImage(currentSprite, (int) (x - FRAME_WIDTH * scaleFactor / 2), (int) (y - FRAME_HEIGHT * scaleFactor / 2),
                (int) (FRAME_WIDTH * scaleFactor), (int) (FRAME_HEIGHT * scaleFactor), null);
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
