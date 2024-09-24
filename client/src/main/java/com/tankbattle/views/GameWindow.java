package com.tankbattle.views;

import javax.swing.JFrame;
import javax.swing.JPanel;

enum Color {
    Red,
    Blue,
    Green,
    Black
}

public class GameWindow extends JFrame {
    private JPanel mainMenuPanel;
    private JPanel gamePanel;

    public GameWindow() {
        super("Tank Battle");

        setSize(800, 800);
        setResizable(false);

        mainMenuPanel = new MenuPanel();

        add(mainMenuPanel);

        setVisible(true);
    }

    public void initializeGameScreen() {
        remove(mainMenuPanel);

        gamePanel = new GamePanel();

        add(gamePanel);

        revalidate();
        repaint();
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }


}
