package com.tankbattle.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tankbattle.models.Player;

public class SpriteTankRenderer implements Renderer {
    private BufferedImage spriteSheet;
    private int spriteWidth;
    private int spriteHeight;
    private double scaleFactor;
    private long lastFrameTime;
    private int currentFrame;
    private static final int FRAME_COUNT = 2;
    private static final int FRAME_DURATION = 60; // 60ms per frame
//"src/main/java/com/tankbattle/assets/images/test_sprite.png"
    public SpriteTankRenderer(double scaleFactor, String spriteSheetPath) {
        try {
            spriteSheet = ImageIO.read(new File(spriteSheetPath));
            spriteWidth = spriteSheet.getWidth();
            spriteHeight = spriteSheet.getHeight() / FRAME_COUNT;
            this.scaleFactor = scaleFactor;
            this.currentFrame = 0;
            this.lastFrameTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getCurrentFrame() {
        int y = currentFrame * spriteHeight;
        return spriteSheet.getSubimage(0, y, spriteWidth, spriteHeight);
    }

    private void updateFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
            lastFrameTime = currentTime;
        }
    }

    @Override
    public void draw(Graphics2D g2d, Player player) {
        updateFrame();
        BufferedImage sprite = getCurrentFrame();

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double rotationAngle = player.getRotationAngle();

        double centerX = sprite.getWidth() / 2.0;
        double centerY = sprite.getHeight() / 2.0;

        AffineTransform oldTransform = g2d.getTransform();

        AffineTransform transform = new AffineTransform();

        transform.translate(x, y);

        transform.rotate(Math.toRadians(rotationAngle));

        transform.scale(scaleFactor, scaleFactor);

        transform.translate(-centerX, -centerY);

        g2d.drawImage(sprite, transform, null);

        g2d.setTransform(oldTransform);
    }
}
