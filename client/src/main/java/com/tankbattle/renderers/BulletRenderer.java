package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.Bullet;
import com.tankbattle.utils.Vector2;

public class BulletRenderer implements EntityRenderer<Bullet>, Scalable {
    private static final int SPRITE_SIZE = 32;
    private final ResourceManager resourceManager;
    private double scaleFactor = 1;
    private double worldLocationScaleFactor = 1.0;
    private Vector2 worldOffset = new Vector2(0, 0);
    private BufferedImage bulletSprite;

    public BulletRenderer(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        loadSprite();
    }

    private void loadSprite() {
        bulletSprite = resourceManager.loadImage("assets/images/bullet.png");
        if (bulletSprite == null) {
            System.err.println("Bullet sprite could not be loaded.");
        }
    }

    @Override
    public void draw(Graphics2D g2d, Bullet bullet) {
        if (bulletSprite == null) return;

        Vector2 location = bullet.getLocation();
        double x = location.getX() * worldLocationScaleFactor + worldOffset.getX();
        double y = location.getY() * worldLocationScaleFactor + worldOffset.getY();

        double centerX = SPRITE_SIZE / 2.0;
        double centerY = SPRITE_SIZE / 2.0;

        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(x, y);
        transform.scale(scaleFactor, scaleFactor);
        transform.translate(-centerX, -centerY);

        g2d.drawImage(bulletSprite, transform, null);
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
