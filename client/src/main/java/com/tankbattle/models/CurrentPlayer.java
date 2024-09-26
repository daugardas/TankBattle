package com.tankbattle.models;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tankbattle.views.GameWindow;

public class CurrentPlayer extends Player implements KeyListener {
    /*
     * We are going to use a bitmask for the movementBuffer.
     *
     * represented as 00000000, we are going to use the lower 4 bits
     *
     * fourth bit - up direction (w)
     * third bit - left direction (a)
     * second bit - down direction (s)
     * first bit - right direction (d)
     */
    private byte movementDirection;
    private byte previousDirection;

    public CurrentPlayer() {
        super();
        this.color = Color.RED;
        movementDirection = 0;
        GameWindow.getInstance().getGamePanel().addKeyListener(this);
    }

    public CurrentPlayer(String username) {
        super(username);
        this.color = Color.RED;
        movementDirection = 0;
        GameWindow.getInstance().getGamePanel().addKeyListener(this);
    }

    public byte getMovementDirection() {
        return movementDirection;
    }

    public byte getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(byte previousDirection) {
        this.previousDirection = previousDirection;
    }

    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                previousDirection = movementDirection;
                movementDirection |= 0b1000;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                previousDirection = movementDirection;
                movementDirection |= 0b0100;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                previousDirection = movementDirection;
                movementDirection |= 0b0010;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                previousDirection = movementDirection;
                movementDirection |= 0b0001;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                previousDirection = movementDirection;
                movementDirection &= 0b0111;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                previousDirection = movementDirection;
                movementDirection &= 0b1011;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                previousDirection = movementDirection;
                movementDirection &= 0b1101;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                previousDirection = movementDirection;
                movementDirection &= 0b1110;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }
}
