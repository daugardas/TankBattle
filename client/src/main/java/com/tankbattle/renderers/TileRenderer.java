package com.tankbattle.renderers;

import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TileRenderer implements EntityRenderer<Tile>, Scalable {
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;

    public TileRenderer() {
        this.scaleFactor = 1;
        this.worldLocationScaleFactor = 1;
    }

    public TileRenderer(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        this.worldLocationScaleFactor = 1;
    }

    @Override
    public void draw(Graphics2D g2d, Tile tile) {
        BufferedImage spriteSheet = tile.getSprite();

        double x = tile.getWorldX() * worldLocationScaleFactor + worldOffset.getX();
        double y = tile.getWorldY() * worldLocationScaleFactor + worldOffset.getY();

        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(x, y);
        transform.scale(scaleFactor, scaleFactor);

        g2d.drawImage(spriteSheet, transform, null);
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
