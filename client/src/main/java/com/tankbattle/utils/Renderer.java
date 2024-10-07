package com.tankbattle.utils;

import java.awt.Graphics2D;

import com.tankbattle.models.Player;

public interface Renderer {
    void draw(Graphics2D g2d, Player player, int panelX, int panelY);
}
