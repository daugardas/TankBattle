package com.tankbattle.models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Tank {
    private Color outlineColor;
    private Color fillColor;
    private int width;
    private int height;

    public Tank(Color outlineColor, Color fillColor, int width, int height) {
        this.outlineColor = outlineColor;
        this.fillColor = fillColor;
        this.width = width;
        this.height = height;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        // Draw tank body
        Shape body = new Rectangle2D.Double(x - width / 2.0, y - height / 2.0, width, height);
        g2d.setColor(fillColor);
        g2d.fill(body);
        g2d.setColor(outlineColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(body);

        // Draw tank turret
        double turretWidth = width * 0.5;
        double turretHeight = height * 0.5;
        Shape turret = new Ellipse2D.Double(x - turretWidth / 2.0, y - turretHeight / 2.0, turretWidth, turretHeight);
        g2d.setColor(fillColor);
        g2d.fill(turret);
        g2d.setColor(outlineColor);
        g2d.draw(turret);

        // Draw tank barrel
        double barrelWidth = width * 0.5;
        double barrelHeight = height * 0.125;
        Shape barrel = new Rectangle2D.Double(x + width / 4.0, y - barrelHeight / 2.0, barrelWidth, barrelHeight);
        g2d.setColor(fillColor);
        g2d.fill(barrel);
        g2d.setColor(outlineColor);
        g2d.draw(barrel);
    }
}
