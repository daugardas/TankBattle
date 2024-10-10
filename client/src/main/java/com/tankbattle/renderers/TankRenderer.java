package com.tankbattle.renderers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.CurrentPlayer;
import com.tankbattle.models.Player;

public class TankRenderer implements EntityRenderer<Player>, Scalable {
    private int spriteWidth;
    private int spriteHeight;
    private double scaleFactor;
    private long lastFrameTime;
    private int currentFrame;
    private static final int FRAME_COUNT = 2;
    private static final int FRAME_DURATION = 60; // 60ms per frame
    private ResourceManager resourceManager;
    private Map<String, BufferedImage> spriteSheetCache = new HashMap<>();

    public TankRenderer(double scaleFactor, ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.scaleFactor = scaleFactor;
        this.currentFrame = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }
    

     private BufferedImage getSpriteSheet(Player player) {
        String spriteSheetPath;
        if (player instanceof CurrentPlayer) {
            spriteSheetPath = "/com/tankbattle/assets/images/player_sprite.png";
        } else {
            spriteSheetPath = "/com/tankbattle/assets/images/enemy_sprite.png";
        }

        return spriteSheetCache.computeIfAbsent(spriteSheetPath, path -> resourceManager.loadImage(path));
    }

    private BufferedImage getCurrentFrame(BufferedImage spriteSheet) {
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
        BufferedImage spriteSheet = getSpriteSheet(player);
        if (spriteSheet != null && (spriteWidth == 0 || spriteHeight == 0)) {
            spriteWidth = spriteSheet.getWidth();
            spriteHeight = spriteSheet.getHeight() / FRAME_COUNT;
        }
        updateFrame();
        BufferedImage sprite = getCurrentFrame(spriteSheet);

        double x = player.getPanelX();
        double y = player.getPanelY();
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

        // Draw username above the tank
        drawUsername(g2d, player.getUsername(), x, y - spriteHeight / 2.0 * scaleFactor +10);
    }

    private void drawUsername(Graphics2D g2d, String username, double x, double y) {
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int usernameWidth = metrics.stringWidth(username);
        int usernameX = (int) (x - usernameWidth / 2);
        int usernameY = (int) y;

        g2d.setColor(Color.BLACK);
        g2d.drawString(username, usernameX, usernameY);
    }

    @Override
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor *4;
    }   
}
