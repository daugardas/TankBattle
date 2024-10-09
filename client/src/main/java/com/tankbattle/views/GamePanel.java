package com.tankbattle.views;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.tankbattle.controllers.GameManager;

public class GamePanel extends JPanel {
    public GamePanel() {
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.clearRect(0, 0, getWidth(), getHeight());

        GameManager.getInstance().renderAll(g2d);
    }
}
