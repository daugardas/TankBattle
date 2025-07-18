package com.tankbattle.input;

import java.util.HashMap;
import java.util.Map;

import com.tankbattle.utils.Constants;

public class GamepadAdapter implements InputHandler {
    private final GamepadInput gamepad;
    private final float DEADZONE_THRESHOLD = 0.2f;

    public GamepadAdapter() {
        gamepad = new GamepadInput();
    }

    public void startInputPolling() {
        gamepad.pollInputs();
    }

    public boolean isInitialized() {
        return gamepad.isInitialized();
    }

    private Map<String, Boolean> processActions(Map<String, Float> inputBuffer) {
        Map<String, Boolean> actions = new HashMap<String, Boolean>();

        if (inputBuffer.get("A") == 1f) {
            actions.put("FIRE", true);
        }

        return actions;
    }

    private byte processMovement(Map<String, Float> inputBuffer) {
        byte movementDirection = 0;

        float x = inputBuffer.get("x");
        float y = inputBuffer.get("y");

        if (Math.abs(x) > DEADZONE_THRESHOLD || Math.abs(y) > DEADZONE_THRESHOLD) {
            Double angleDeg = Math.toDegrees(Math.atan2(y, x));

            if (angleDeg >= -157.5 && angleDeg < -112.5) {
                movementDirection = (Constants.DIRECTION_LEFT | Constants.DIRECTION_UP);
            } else if (angleDeg >= -112.5 && angleDeg < -67.5) {
                movementDirection = Constants.DIRECTION_UP; // Right (D)
            } else if (angleDeg >= -67.5 && angleDeg < -22.5) {
                movementDirection = (Constants.DIRECTION_UP | Constants.DIRECTION_RIGHT);
            } else if (angleDeg >= -22.5 && angleDeg < 22.5) {
                movementDirection = Constants.DIRECTION_RIGHT;
            } else if (angleDeg >= 22.5 && angleDeg < 67.5) {
                movementDirection = (Constants.DIRECTION_RIGHT | Constants.DIRECTION_DOWN);
            } else if (angleDeg >= 67.5 && angleDeg < 112.5) {
                movementDirection = Constants.DIRECTION_DOWN;
            } else if (angleDeg >= 112.5 && angleDeg < 157.5) {
                movementDirection = (Constants.DIRECTION_DOWN | Constants.DIRECTION_LEFT);
            } else if (angleDeg >= 157.5 || angleDeg < -157.5) {
                movementDirection = (Constants.DIRECTION_LEFT);
            }
        }

        return movementDirection;
    }

    @Override
    public InputData handleInput() {
        Map<String, Float> inputBuffer = gamepad.getInputBuffer();

        byte movementDirection = processMovement(inputBuffer);
        Map<String, Boolean> actions = processActions(inputBuffer);

        return new InputData(movementDirection, actions);
    }

}
