package com.tankbattle.models;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.tankbattle.commands.FireCommand;
import com.tankbattle.controllers.GameManager;
import com.tankbattle.input.GamepadAdapter;
import com.tankbattle.input.InputData;
import com.tankbattle.input.KeyboardAdapter;
import com.tankbattle.input.KeyboardInput;
import com.tankbattle.utils.Vector2;
import com.tankbattle.views.GameWindow;

public class CurrentPlayer extends Player {

    private final Thread gamepadInputPollThread;

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

    private KeyboardAdapter keyboard;
    private GamepadAdapter gamepad;

    public CurrentPlayer(String username) {
        super(username);

        this.movementDirection = 0;
        keyboard = new KeyboardAdapter();
        gamepad = new GamepadAdapter();

        if (gamepad.isInitialized()) {
            System.out.println("Gamepad found, enabling gamepad controls");

            this.gamepadInputPollThread = new Thread(() -> {
                gamepad.startInputPolling();
            });

            gamepadInputPollThread.start();
        } else {
            System.out.println("Gamepad wasn't found, gamepad controls are not enabled");
            gamepadInputPollThread = null;
        }

    }

    public KeyboardAdapter getKeyboard() {
        return keyboard;
    }

    public InputData getInputData() {
        if (gamepad.isInitialized()) {
            InputData gamepadData = gamepad.handleInput();
            InputData keyboardData = keyboard.handleInput();

            if (gamepadData.isEmpty() && keyboardData.isEmpty()) {
                return keyboardData;
            } else if (gamepadData.isEmpty() && !keyboardData.isEmpty()) {
                return keyboardData;
            } else {
                return gamepadData;
            }

        } else {
            return keyboard.handleInput();
        }
    }

    public byte getMovementDirection() {
        return movementDirection;
    }

    public void setMovementDirection(byte movementDirection) {
        this.movementDirection = movementDirection;
    }

    public byte getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(byte previousDirection) {
        this.previousDirection = previousDirection;
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.CURRENT_PLAYER;
    }
}
