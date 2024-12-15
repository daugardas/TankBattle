package com.tankbattle.renderers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.PowerUp;
import com.tankbattle.utils.Vector2;

public class PowerUpRenderer implements EntityRenderer<PowerUp>, Scalable {
    private static final int FRAME_COUNT = 4;
    private static final int FRAME_DURATION = 500;  // 500ms = 2 FPS
    private static final int SPRITE_SIZE = 32;  // 32x32 frames
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;
    private int currentFrame;
    private long lastFrameTime;
    private final ResourceManager resourceManager;
    private BufferedImage spriteSheet;

    public PowerUpRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = 2;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
        loadSprite();
    }

    private void loadSprite() {
        spriteSheet = resourceManager.loadImage("assets/images/power-up.png");
    }

    private BufferedImage getCurrentFrame() {
        if (spriteSheet == null) return null;
        int y = currentFrame * SPRITE_SIZE;
        return spriteSheet.getSubimage(0, y, SPRITE_SIZE, SPRITE_SIZE);
    }

    private void updateFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
            lastFrameTime = currentTime;
        }
    }

    @Override
    public void draw(Graphics2D g2d, PowerUp powerUp) {
        updateFrame();
        BufferedImage sprite = getCurrentFrame();
        if (sprite == null) return;

        double x = powerUp.getLocation().getX() * worldLocationScaleFactor + worldOffset.getX();
        double y = powerUp.getLocation().getY() * worldLocationScaleFactor + worldOffset.getY();

        double centerX = SPRITE_SIZE / 2.0;
        double centerY = SPRITE_SIZE / 2.0;

        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(x, y);
        transform.scale(scaleFactor, scaleFactor);
        transform.translate(-centerX, -centerY);

        g2d.drawImage(sprite, transform, null);

        // Draw power-up type text
        drawPowerUpType(g2d, powerUp.getType(), x, y - SPRITE_SIZE * scaleFactor / 2);

        g2d.setTransform(oldTransform);
    }

    private void drawPowerUpType(Graphics2D g2d, String type, double x, double y) {
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        String displayText = type.toLowerCase();
        int textWidth = metrics.stringWidth(displayText);
        int textX = (int) (x - textWidth / 2);
        int textY = (int) y;

        // Draw text with outline for better visibility
        g2d.setColor(Color.BLACK);
        g2d.drawString(displayText, textX - 1, textY);
        g2d.drawString(displayText, textX + 1, textY);
        g2d.drawString(displayText, textX, textY - 1);
        g2d.drawString(displayText, textX, textY + 1);

        g2d.setColor(Color.WHITE);
        g2d.drawString(displayText, textX, textY);
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