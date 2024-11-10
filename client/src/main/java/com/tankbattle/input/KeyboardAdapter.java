package com.tankbattle.input;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tankbattle.utils.Constants;
import com.tankbattle.views.GameWindow;

public class KeyboardAdapter implements InputHandler {
    private final KeyboardInput keyboard;

    public KeyboardAdapter() {
        keyboard = new KeyboardInput();
        GameWindow.getInstance().getGamePanel().addKeyListener(keyboard);
    }

    @Override
    public InputData handleInput() {
        Set<Integer> inputBuffer = keyboard.getInputBuffer();

        byte movementDirection = 0;
        Map<String, Boolean> actions = new HashMap<String,Boolean>();

        for (Integer input : inputBuffer) {
            switch (input) {
                case KeyEvent.VK_W, KeyEvent.VK_UP -> movementDirection |= Constants.DIRECTION_UP;
                case KeyEvent.VK_A, KeyEvent.VK_LEFT -> movementDirection |= Constants.DIRECTION_LEFT;
                case KeyEvent.VK_S, KeyEvent.VK_DOWN -> movementDirection |= Constants.DIRECTION_DOWN;
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> movementDirection |= Constants.DIRECTION_RIGHT;
                case KeyEvent.VK_SPACE -> actions.put("FIRE", true);
            }
        }

        return new InputData(movementDirection, actions);
    }

}
