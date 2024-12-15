package com.tankbattle.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.tankbattle.models.Player;
import com.tankbattle.mediator.ScoreMediatorImpl;

public class ScoreBoard extends JPanel {
    private List<Player> players = new ArrayList<>();
    private static final int PANEL_WIDTH = 150;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 128);
    
    public ScoreBoard() {
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setOpaque(false);
        ScoreMediatorImpl.getInstance().registerScoreBoard(this);
    }

    public void updatePlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw semi-transparent background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Enable anti-aliasing for text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw scores
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        
        int y = 25;
        g2d.drawString("SCORES", 10, y);
        y += 20;
        
        for (Player player : players) {
            String text = player.getUsername() + ": " + player.getScore();
            g2d.drawString(text, 10, y);
            y += 20;
        }
    }
} 