package com.tankbattle.utils;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import com.tankbattle.models.Player;

public class VectorTankRenderer implements Renderer {

    @Override
    public void draw(Graphics2D g2d, Player player) {
        int x = player.getLocation().getX();
        int y = player.getLocation().getY();
        int width = player.getSize().getX();
        int height = player.getSize().getY();
        double rotationAngle = player.getRotationAngle();

        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(rotationAngle), x, y);

        // Draw tank body
        Shape body = new Rectangle2D.Double(x - width / 2.0, y - height / 2.0, width, height);
        g2d.setColor(player.getFillColor());
        g2d.fill(body);
        g2d.setColor(player.getOutlineColor());
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(body);

        // Draw tank turret
        double turretWidth = width * 0.5;
        double turretHeight = height * 0.5;
        Shape turret = new Ellipse2D.Double(x - turretWidth / 2.0, y - turretHeight / 2.0, turretWidth, turretHeight);
        g2d.setColor(player.getFillColor());
        g2d.fill(turret);
        g2d.setColor(player.getOutlineColor());
        g2d.draw(turret);

        // Draw tank barrel
        double barrelWidth = width * 0.5;
        double barrelHeight = height * 0.125;
        Shape barrel = new Rectangle2D.Double(x + width / 4.0, y - barrelHeight / 2.0, barrelWidth, barrelHeight);
        g2d.setColor(player.getFillColor());
        g2d.fill(barrel);
        g2d.setColor(player.getOutlineColor());
        g2d.draw(barrel);

        g2d.setTransform(oldTransform);
    }
}
