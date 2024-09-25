package com.tankbattle.models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.views.GameWindow;

public class CurrentPlayer extends Player implements KeyListener {
    private int[] movementBuffer;

    public CurrentPlayer() {
        super();
        movementBuffer = new int[2];
        GameWindow.getInstance().getGamePanel().addKeyListener(this);
    }

    public CurrentPlayer(String username) {
        super(username);
        movementBuffer = new int[2];
        GameWindow.getInstance().getGamePanel().addKeyListener(this);
    }

    public int[] getMovementBuffer() {
        return movementBuffer;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                movementBuffer[1] = -1;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                movementBuffer[1] = 1;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                movementBuffer[0] = 1;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                movementBuffer[0] = -1;
                break;
        }

        if (movementBuffer[0] != 0 || movementBuffer[1] != 0) {
            GameManager.getInstance().sendMovementBuffer(movementBuffer);
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                movementBuffer[1] = 0;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                movementBuffer[0] = 0;
                break;
        }

        GameManager.getInstance().sendMovementBuffer(movementBuffer);
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(location.x, location.y, size, size);
    }
}
