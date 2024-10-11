package com.tankbattle.views;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
    private JPanel mainMenuPanel;
    private GamePanel gamePanel;

    private GameWindow() {
        super("Tank Battle");
        setSize(800, 800);
        setResizable(true);

        mainMenuPanel = new MenuPanel();
        gamePanel = new GamePanel();

        add(mainMenuPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onResize();
            }
        });

        setVisible(true);
    }

    private static final GameWindow INSTANCE = new GameWindow();

    public static GameWindow getInstance() {
        return INSTANCE;
    }

    public void initializeGameScreen() {
        remove(mainMenuPanel);
        add(gamePanel);

        gamePanel.requestFocusInWindow();

        revalidate();
        repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    private void onResize(){
        // do something in the future?
    }

    public void setGamePanelWorldSize(int width, int height) {
        gamePanel.setWorldSize(width, height);
    }
}
