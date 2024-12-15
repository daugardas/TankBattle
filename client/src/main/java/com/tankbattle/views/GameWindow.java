package com.tankbattle.views;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    private static final GameWindow INSTANCE = new GameWindow();
    private final MenuPanel mainMenuPanel;
    private final GamePanel gamePanel;

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static GameWindow getInstance() {
        return INSTANCE;
    }

    public void initializeGameScreen() {
        System.out.println("Initializing game screen");
        remove(mainMenuPanel);
        add(gamePanel);

        gamePanel.requestFocusInWindow();

        revalidate();
        repaint();
    }

    public void resetToMenu() {
        remove(this.gamePanel);
        add(this.mainMenuPanel);
        this.mainMenuPanel.resetPanel();
        this.mainMenuPanel.requestFocusInWindow();
        revalidate();
        repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    private void onResize() {
        // do something in the future?
    }

    public void setGamePanelWorldSize(int width, int height) {
        gamePanel.setWorldSize(width, height);
    }
}
