package com.tankbattle.utils;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
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

        // Save the current transform
        AffineTransform oldTransform = g2d.getTransform();

        // Rotate the canvas based on the player's rotation angle
        g2d.rotate(Math.toRadians(rotationAngle), x, y);

        // Set the fill color once for all filled shapes
        g2d.setColor(player.getFillColor());

        // 1. Draw the tank treads (two large rectangles extending equally in front and back)
        double treadWidth = width * 1.2;  // Treads extend beyond the body width
        double treadHeight = height * 0.2; // Reduced tread height to match the narrower body height

        // Calculate body dimensions
        double bodyHeight = height * 0.8; // Reduced body height
        double bodyTopY = y - bodyHeight / 2.0;
        double bodyBottomY = y + bodyHeight / 2.0;

        // Adjust tread positions to align with the body
        double leftTreadY = bodyTopY - treadHeight;
        double rightTreadY = bodyBottomY;

        // Calculate tread X position
        double treadX = x - treadWidth / 2.0;

        // Left tread
        Shape leftTread = new Rectangle2D.Double(treadX, leftTreadY, treadWidth, treadHeight);
        g2d.fill(leftTread); // Fill the left tread with color

        // Right tread
        Shape rightTread = new Rectangle2D.Double(treadX, rightTreadY, treadWidth, treadHeight);
        g2d.fill(rightTread); // Fill the right tread with color

        // Set outline color and stroke for treads
        g2d.setColor(player.getOutlineColor());
        g2d.setStroke(new BasicStroke(2)); // Set stroke thickness for the outline

        // Draw the outline of the treads
        g2d.draw(leftTread);
        g2d.draw(rightTread);

        // Draw dividing lines on treads
        g2d.setStroke(new BasicStroke(1)); // Set stroke thickness for the lines

        // For each tread, draw 4 lines to divide it into 5 segments
        for (int i = 1; i <= 4; i++) {
            double lineX = treadX + (treadWidth / 5.0) * i;
            // Left tread dividing lines
            g2d.drawLine((int) lineX, (int) leftTreadY, (int) lineX, (int) (leftTreadY + treadHeight));
            // Right tread dividing lines
            g2d.drawLine((int) lineX, (int) rightTreadY, (int) lineX, (int) (rightTreadY + treadHeight));
        }

        // 2. Draw the tank body (make it a bit more narrow in height)
        Shape body = new Rectangle2D.Double(x - width / 2.0, bodyTopY, width, bodyHeight);
        g2d.setColor(player.getFillColor()); // Ensure fill color is set
        g2d.fill(body); // Fill the body with color

        // Draw the outline of the body
        g2d.setColor(player.getOutlineColor()); // Set outline color
        g2d.setStroke(new BasicStroke(2)); // Set stroke thickness
        g2d.draw(body);

        // 3. Draw the turret (adjust height to match the narrower body)
        double turretWidth = width * 0.4; // Turret width remains the same
        double turretHeight = bodyHeight * 0.5; // Adjust turret height relative to new body height
        Shape turret = new Rectangle2D.Double(x - turretWidth / 2.0, y - turretHeight / 2.0, turretWidth, turretHeight);
        g2d.setColor(player.getFillColor()); // Set fill color for the turret
        g2d.fill(turret); // Fill the turret with color
        g2d.setColor(player.getOutlineColor()); // Set outline color for the turret
        g2d.draw(turret); // Draw the outline of the turret

        // 4. Draw the barrel (positioned relative to the turret)
        double barrelWidth = width * 0.6; // Barrel length remains the same
        double barrelHeight = bodyHeight * 0.1; // Adjust barrel height to match new body height
        Shape barrel = new Rectangle2D.Double(x + turretWidth / 2.0, y - barrelHeight / 2.0, barrelWidth, barrelHeight);
        g2d.setColor(player.getFillColor()); // Set fill color for the barrel
        g2d.fill(barrel); // Fill the barrel with color
        g2d.setColor(player.getOutlineColor()); // Set outline color for the barrel
        g2d.draw(barrel); // Draw the outline of the barrel

        // Restore the original transform (rotation)
        g2d.setTransform(oldTransform);
    }
}
