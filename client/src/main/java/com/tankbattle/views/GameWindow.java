package com.tankbattle.views;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
    private JPanel mainMenuPanel;
    private JPanel gamePanel;

    private GameWindow() {
        super("Tank Battle");
        setSize(800, 800);
        setResizable(false);

        mainMenuPanel = new MenuPanel();
        gamePanel = new GamePanel();

        add(mainMenuPanel);

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

    public JPanel getGamePanel() {
        return gamePanel;
    }
}
