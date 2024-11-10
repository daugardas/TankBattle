package com.tankbattle.input;

import java.util.Map;

public class InputData {
    private final byte movementDirection;
    private final Map<String, Boolean> actions;

    public InputData() {
        movementDirection = 0;
        actions = null;
    }

    public InputData(byte movementDirection, Map<String, Boolean> actions) {
        this.movementDirection = movementDirection;
        this.actions = actions;
    }

    public byte getMovementDirection() {
        return movementDirection;
    }

    public Map<String, Boolean> getActions() {
        return actions;
    }

    public boolean isEmpty() {
        return movementDirection == 0 && actions.size() == 0;
    }

    public String toString() {
        return "{ movementDirection: " + movementDirection + "; actions: " + actions.toString() + " }";
    }
}
