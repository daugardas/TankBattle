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
    private byte movementDirection;
    private byte previousDirection;

     public CurrentPlayer(String username, Renderer renderer, Vector2 location, Vector2 size, Color outlineColor, Color fillColor) {
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
        updateRotationAngle();
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
        updateRotationAngle();
    }

    @Override
    public void keyTyped(KeyEvent key) {
    }

    private void updateRotationAngle() {
        switch (movementDirection) {
            case 0b1000: // Up
                setRotationAngle(270);
                break;
            case 0b0100: // Left
                setRotationAngle(180);
                break;
            case 0b0010: // Down
                setRotationAngle(90);
                break;
            case 0b0001: // Right
                setRotationAngle(0);
                break;
            case 0b1001: // Up-Right
                setRotationAngle(315);
                break;
            case 0b1100: // Up-Left
                setRotationAngle(225);
                break;
            case 0b0110: // Down-Left
                setRotationAngle(135);
                break;
            case 0b0011: // Down-Right
                setRotationAngle(45);
                break;
        }
    }
}
