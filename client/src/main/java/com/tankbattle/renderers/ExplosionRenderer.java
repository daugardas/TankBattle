package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.Collision;
import com.tankbattle.utils.Vector2;

public class ExplosionRenderer implements EntityRenderer<Collision>, Scalable {
    private static final int SPRITE_SIZE = 32;
    private static final int FRAME_COUNT = 5;
    private static final int FRAME_DURATION = 200;  // 200ms = 5 FPS
    private static final int LAST_FRAME_EXTRA_DURATION = 2000;  // Last frame stays 2 seconds longer
    
    private final ResourceManager resourceManager;
    private double scaleFactor = 1;
    private double worldLocationScaleFactor = 1.0;
    private Vector2 worldOffset = new Vector2(0, 0);
    private BufferedImage[] explosionFrames;

    public ExplosionRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        loadSpriteSheet();
    }

    private void loadSpriteSheet() {
        BufferedImage spriteSheet = resourceManager.loadImage("assets/images/Explosion.png");
        if (spriteSheet == null) {
            System.err.println("Explosion sprite sheet could not be loaded.");
            return;
        }

        explosionFrames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            explosionFrames[i] = spriteSheet.getSubimage(0, i * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        }
    }

    private int getCurrentFrame(long startTime) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        
        // Calculate total animation time excluding last frame
        long regularAnimationTime = (FRAME_COUNT - 1) * FRAME_DURATION;
        
        if (elapsedTime < regularAnimationTime) {
            // Still in regular animation
            return (int) (elapsedTime / FRAME_DURATION);
        } else {
            // In last frame
            long timeInLastFrame = elapsedTime - regularAnimationTime;
            if (timeInLastFrame <= LAST_FRAME_EXTRA_DURATION) {
                return FRAME_COUNT - 1;  // Stay on last frame
            } else {
                return FRAME_COUNT;  // Past animation end
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d, Collision collision) {
        if (explosionFrames == null) return;

        int currentFrame = getCurrentFrame(collision.getStartTime());
        BufferedImage sprite = explosionFrames[currentFrame];

        Vector2 location = collision.getLocation();
        double x = location.getX() * worldLocationScaleFactor + worldOffset.getX();
        double y = location.getY() * worldLocationScaleFactor + worldOffset.getY();

        double centerX = SPRITE_SIZE / 2.0;
        double centerY = SPRITE_SIZE / 2.0;

        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(x, y);
        transform.scale(scaleFactor, scaleFactor);
        transform.translate(-centerX, -centerY);

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
