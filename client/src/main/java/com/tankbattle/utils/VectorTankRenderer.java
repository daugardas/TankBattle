package com.tankbattle.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.tankbattle.models.Player;
import com.tankbattle.models.Tank;

public class VectorTankRenderer implements Renderer {
    @Override
    public void draw(Graphics2D g2d, Player player) {
        Tank tank = player.getTank();
        int x = player.getLocation().getX();
        int y = player.getLocation().getY();
        double rotationAngle = player.getRotationAngle();

        AffineTransform oldTransform = g2d.getTransform();

        g2d.rotate(Math.toRadians(rotationAngle), x, y);
        tank.draw(g2d, x, y);
        g2d.setTransform(oldTransform);
    }
}
