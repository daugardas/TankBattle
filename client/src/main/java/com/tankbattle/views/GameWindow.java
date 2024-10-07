package com.tankbattle.views;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
    private JPanel mainMenuPanel;
    private JPanel gamePanel;

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

    public JPanel getGamePanel() {
        return gamePanel;
    }

    private void onResize(){
        System.out.println("Window resized to: " + getSize());
    }
}
