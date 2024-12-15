package com.tankbattle.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.tankbattle.mediator.ScoreMediatorImpl;
import com.tankbattle.models.Player;

public class ScoreBoard extends JPanel {
    private List<Player> players = new ArrayList<>();
    private static final int PANEL_WIDTH = 200;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 200);
    private static final Color BORDER_COLOR = new Color(0, 255, 0);
    private static final Color TEXT_COLOR = new Color(0, 255, 0);
    private Font pixelFont;
    
    public ScoreBoard() {
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setOpaque(false);
        ScoreMediatorImpl.getInstance().registerScoreBoard(this);
        
        // Create a pixelated font
        try {
            pixelFont = new Font("Courier New", Font.BOLD, 16);
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.BOLD, 16);
        }
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
        
        // Enable pixel-perfect rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_SPEED);
        
        // Draw background
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.drawRect(2, 2, getWidth()-4, getHeight()-4);
        g2d.drawRect(4, 4, getWidth()-8, getHeight()-8);
        
        // Draw header
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(pixelFont);
        drawPixelText(g2d, "HIGH SCORES", 20, 30);
        
        // Draw separator
        drawPixelLine(g2d, 10, 40, getWidth()-20, 40);
        
        // Draw scores
        int y = 70;
        int rank = 1;
        for (Player player : players) {
            String rankStr = String.format("%d.", rank++);
            String nameStr = truncateString(player.getUsername(), 12);
            String scoreStr = String.format("%06d", player.getScore());
            
            // Draw rank and username on first line
            drawPixelText(g2d, rankStr, 15, y);
            drawPixelText(g2d, nameStr, 40, y);
            
            // Draw score centered on second line
            int scoreWidth = g2d.getFontMetrics().stringWidth(scoreStr);
            int scoreX = (getWidth() - scoreWidth) / 2;
            drawPixelText(g2d, scoreStr, scoreX, y + 20);
            
            // Increase y more to account for two lines
            y += 45;
        }
    }
    
    private void drawPixelText(Graphics2D g2d, String text, int x, int y) {
        // Draw text with a slight shadow for retro effect
        g2d.setColor(new Color(0, 100, 0));
        g2d.drawString(text, x + 1, y + 1);
        g2d.setColor(TEXT_COLOR);
        g2d.drawString(text, x, y);
    }
    
    private void drawPixelLine(Graphics2D g2d, int x1, int y, int x2, int y2) {
        // Draw dotted line for retro effect
        for (int x = x1; x < x2; x += 2) {
            g2d.drawLine(x, y, x, y2);
        }
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 2) + "..";
    }
} 