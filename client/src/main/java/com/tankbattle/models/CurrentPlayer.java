package com.tankbattle.models;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tankbattle.utils.Renderer;
import com.tankbattle.utils.Vector2;
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
    private static final byte DIRECTION_UP = 0b1000;
    private static final byte DIRECTION_LEFT = 0b0100;
    private static final byte DIRECTION_DOWN = 0b0010;
    private static final byte DIRECTION_RIGHT = 0b0001;

    private byte movementDirection;
    private byte previousDirection;

    public CurrentPlayer(String username, Renderer renderer, Vector2 location, Vector2 size, Color outlineColor,
                         Color fillColor) {
        super(username, renderer, location, size, outlineColor, fillColor);
        this.movementDirection = 0;
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
        previousDirection = movementDirection;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                movementDirection |= DIRECTION_UP;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                movementDirection |= DIRECTION_LEFT;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                movementDirection |= DIRECTION_DOWN;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                movementDirection |= DIRECTION_RIGHT;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        previousDirection = movementDirection;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                movementDirection &= ~DIRECTION_UP;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                movementDirection &= ~DIRECTION_LEFT;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                movementDirection &= ~DIRECTION_DOWN;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                movementDirection &= ~DIRECTION_RIGHT;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent key) {
        // Not used
    }
}
