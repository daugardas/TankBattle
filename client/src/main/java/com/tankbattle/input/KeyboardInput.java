package com.tankbattle.input;

import java.util.HashSet;
import java.util.Set;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
    private Set<Integer> inputBuffer;

    public KeyboardInput() {
        inputBuffer = new HashSet<>();
    }

    public Set<Integer> getInputBuffer() {
        return inputBuffer;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent key) {
        inputBuffer.add(key.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent key) {
        inputBuffer.remove((Integer) key.getKeyCode());
    }
}
