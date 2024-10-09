package com.tankbattle.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.models.Player;

public class GamePanel extends JPanel {
    private int WORLD_WIDTH = 800; // default world size
    private int WORLD_HEIGHT = 800; // default world size
    private int scaledWorldWidth = 800;
    private int scaledWorldHeight = 800;
    private int offsetX = 0;
    private int offsetY = 0;
    private float scaleFactor = 1;

    public GamePanel() {
        
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        updateScaleFactor();
        updateOffsets();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScaleFactor();
                updateOffsets();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        ArrayList<Player> allPlayers = GameManager.getInstance().getAllPlayers();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(offsetX, offsetY, scaledWorldWidth,
                scaledWorldHeight);

        allPlayers.forEach(player -> {
            int panelX = worldToPanelX(player.getLocation().getX());
            int panelY = worldToPanelY(player.getLocation().getY());
            player.setPanelX(panelX);
            player.setPanelY(panelY);
        });
        GameManager.getInstance().renderAll(g2d);

        // draw world borders (this is drawn around the world,
        // because parts of the tank would be drawn outside the
        // world border on the background)
        g2d.setColor(Color.DARK_GRAY);
        // for when the window width > height
        g2d.fillRect(0, 0, offsetX - 1, getHeight());
        g2d.fillRect(worldToPanelX(WORLD_HEIGHT) + 1, 0, getWidth(), getHeight());
        // for when the window height > width
        g2d.fillRect(0, worldToPanelY(WORLD_HEIGHT) + 1, getWidth(), getHeight());
        g2d.fillRect(0, 0, getWidth(), offsetY - 1);
    }

    private void updateOffsets() {
        offsetX = Math.round(((float) getWidth() - (float) scaledWorldWidth) / 2.0f);
        offsetY = Math.round(((float) getHeight() - (float) scaledWorldHeight) / 2.0f);

        System.out.println("offsetX: " + offsetX + " offsetY: " + offsetY);
    }

    private void updateScaleFactor() {
        float widthRatio = getWidth() / (float) WORLD_WIDTH;
        float heightRatio = getHeight() / (float) WORLD_HEIGHT;

        scaleFactor = Math.min(widthRatio, heightRatio);
        scaledWorldWidth = Math.round(WORLD_WIDTH * scaleFactor);
        scaledWorldHeight = Math.round(WORLD_HEIGHT * scaleFactor);

        System.out.println("scaleFactor: " + scaleFactor);
        System.out.println("scaledWorldWidth: " + scaledWorldWidth + " scaledWorldHeight: " + scaledWorldHeight);
        GameManager.getInstance().setScaleFactor(scaleFactor);
    }

    private int worldToPanelX(float worldX) {
        return Math.round(worldX * scaleFactor) + offsetX;
    }

    private int worldToPanelY(float worldY) {
        return Math.round(worldY * scaleFactor) + offsetY;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }
}
