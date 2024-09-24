package com.tankbattle.views;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.models.Player;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    public GamePanel() {
        setLayout(null);
        Timer timer = new Timer(16, e -> this.repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        ArrayList<Player> allPlayers = GameManager.getInstance().getAllPlayers();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        allPlayers.forEach(player -> player.draw(g));
    }
}
