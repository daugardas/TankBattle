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
    JPanel mainMenuPanel;

    public GameWindow() {
        super("Tank Battle");
        setSize(800, 800);
        setResizable(false);

        mainMenuPanel = new MenuPanel();

        add(mainMenuPanel);

        setVisible(true);
    }
}
