package com.tankbattle.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.utils.ClientFPSCounter;
import com.tankbattle.utils.ServerFPSCounter;
import com.tankbattle.utils.Vector2;

public class GamePanel extends JPanel {
    private int WORLD_WIDTH = 10000; // default world size
    private int WORLD_HEIGHT = 10000; // default world size
    private int panelWorldWidth = 800;
    private int panelWorldHeight = 800;
    private int offsetX = 0;
    private int offsetY = 0;

    // this scale factor is used to scale sprites and other graphics
    private float rendererScaleFactor = 1;

    // this scale factor is used to convert world coordinates to panel coordinates.
    // Do not use this to scale sprites or other graphics
    private float worldToPanelScaleFactor = 1;

    public GamePanel() {

        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        updateScaleFactor();
        updateOffsets();

        ClientFPSCounter.getInstance().start();

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

        ClientFPSCounter.getInstance().incrementFrameCount();

        // TODO: Rendering is done in one thread, fix it to use multiple threads
        // we can optimize this by rendering images to buffers in seperate
        // threads and then drawing them all at once, this way we can avoid the 
        // overhead of drawing each image individually

        BufferedImage clearedPanelImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage entitiesBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage worldBordersBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        BufferedImage fpsBufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        CountDownLatch latch = new CountDownLatch(4);

        SwingWorker<Void, Void> clearPanelWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    clearPanel(clearedPanelImage.createGraphics());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
                return null;
            }

            @Override
            protected void done() {
            }
        };

        SwingWorker<Void, Void> renderEntitiesWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    renderEntities(entitiesBufferedImage.createGraphics());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
                return null;
            }

            @Override
            protected void done() {}
        };

        SwingWorker<Void, Void> drawWorldBordersWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    drawWorldBorders(worldBordersBufferedImage.createGraphics());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
                return null;
            }

            @Override
            protected void done() {
            }
        };

        SwingWorker<Void, Void> drawFPSWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    var g = fpsBufferedImage.createGraphics();
                    drawClientFPS(g);
                    drawServerFPS(g);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
                return null;
            }

            @Override
            protected void done() {
            }
        };

        clearPanelWorker.execute();
        renderEntitiesWorker.execute();
        drawWorldBordersWorker.execute();
        drawFPSWorker.execute();

        // Wait for all workers to finish
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        g2d.drawImage(clearedPanelImage, 0, 0, null);
        g2d.drawImage(entitiesBufferedImage, 0, 0, null);
        g2d.drawImage(worldBordersBufferedImage, 0, 0, null);
        g2d.drawImage(fpsBufferedImage, 0, 0, null);
    }

    private void clearPanel(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(offsetX, offsetY, panelWorldWidth, panelWorldHeight);
    }

    private void renderEntities(Graphics2D g2d) {
        GameManager.getInstance().renderAll(g2d);
    }

    private void drawWorldBorders(Graphics2D g2d) {
        // this is drawn around the world,
        // because parts of the tank would be drawn outside the
        // world border on the background
        g2d.setColor(Color.DARK_GRAY);
        // for when the window width > height
        g2d.fillRect(0, 0, offsetX - 1, getHeight());
        g2d.fillRect(worldToPanelX(WORLD_HEIGHT) + 1, 0, getWidth(), getHeight());
        // for when the window height > width
        g2d.fillRect(0, worldToPanelY(WORLD_HEIGHT) + 1, getWidth(), getHeight());
        g2d.fillRect(0, 0, getWidth(), offsetY - 1);
    }

    private void drawClientFPS(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.setColor(Color.RED);
        String fpsText = String.format("FPS: %.2f", ClientFPSCounter.getInstance().getFps());
        int stringWidth = g2d.getFontMetrics().stringWidth(fpsText);
        int xPosition = getWidth() - stringWidth - 20;
        int yPosition = 40;
        g2d.drawString(fpsText, xPosition, yPosition);
    }

    private void drawServerFPS(Graphics2D g2d) {
        float serverFps = ServerFPSCounter.getInstance().getServerFps();
        String serverFpsText = String.format("Server: %.2f", serverFps);
        int serverFpsStringWidth = g2d.getFontMetrics().stringWidth(serverFpsText);
        int serverFpsXPosition = getWidth() - serverFpsStringWidth - 20;
        int serverFpsYPosition = 80;
        g2d.drawString(serverFpsText, serverFpsXPosition, serverFpsYPosition);
    }

    private void updateOffsets() {
        offsetX = Math.round(((float) getWidth() - (float) panelWorldWidth) / 2.0f);
        offsetY = Math.round(((float) getHeight() - (float) panelWorldHeight) / 2.0f);

        GameManager.getInstance().setWorldOffset(new Vector2(offsetX, offsetY));
    }

    private void updateScaleFactor() {
        int panelSize = Math.min(getWidth(), getHeight());

        float widthRatio = panelSize / (float) WORLD_WIDTH;
        float heightRatio = panelSize / (float) WORLD_HEIGHT;

        worldToPanelScaleFactor = Math.min(widthRatio, heightRatio);
        panelWorldWidth = Math.round(WORLD_WIDTH * worldToPanelScaleFactor);
        panelWorldHeight = Math.round(WORLD_HEIGHT * worldToPanelScaleFactor);

        // drawing scale factor is calculated based on the drawn tile size. Default
        // sprite size is 32x32, so we need to scale it to the tile size;
        rendererScaleFactor = Math.min(getPanelTileWidth(), getPanelTileHeight()) / 32.0f;
        // with this scaling, high DPI displays will have a better rendering,
        // as the sprites will be scaled up

        GameManager.getInstance().setRenderingScaleFactor(rendererScaleFactor);
        GameManager.getInstance().setWorldLocationScaleFactor(worldToPanelScaleFactor);
    }

    private int worldToPanelX(float worldX) {
        return Math.round(worldX * worldToPanelScaleFactor) + offsetX;
    }

    private int worldToPanelY(float worldY) {
        return Math.round(worldY * worldToPanelScaleFactor) + offsetY;
    }

    public float getWorldToPanelScaleFactor() {
        return worldToPanelScaleFactor;
    }

    public int getPanelWorldWidth() {
        return panelWorldWidth;
    }

    public int getPanelWorldHeight() {
        return panelWorldHeight;
    }

    public int getPanelWorldSize() {
        return Math.min(panelWorldWidth, panelWorldHeight);
    }

    public int getWorldWidthInTiles() {
        return WORLD_WIDTH / 1000; // according to the coordinate system, every 1000 units is a tile
    }

    public int getWorldHeightInTiles() {
        return WORLD_HEIGHT / 1000; // according to the coordinate system, every 1000 units is a tile
    }

    public int getWorldWidth() {
        return WORLD_WIDTH;
    }

    public int getWorldHeight() {
        return WORLD_HEIGHT;
    }

    public int getPanelTileWidth() {
        return panelWorldWidth / getWorldWidthInTiles();
    }

    public int getPanelTileHeight() {
        return panelWorldHeight / getWorldHeightInTiles();
    }

    public void setWorldSize(int width, int height) {
        this.WORLD_WIDTH = width;
        this.WORLD_HEIGHT = height;
        updateScaleFactor();
        updateOffsets();
    }
}
