package com.tankbattle.input;

import java.util.HashMap;
import java.util.Map;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class GamepadInput {
    private Controller gamepad;
    private Map<String, Float> inputBuffer;

    public GamepadInput() {
        inputBuffer = new HashMap<>();
        /* Get the available controllers */
        Controller[] controllers = ControllerEnvironment
                .getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            System.out.println("Found no controllers.");
            gamepad = null;
            return;
        }

        for (int i = 0; i < controllers.length; i++) {
            if (controllers[i].getType() == Controller.Type.GAMEPAD) {
                gamepad = controllers[i];
                break;
            }

        }
    }

    public boolean isInitialized(){
        return gamepad != null;
    }

    public Map<String, Float> getInputBuffer() {
        return inputBuffer;
    }

    public void pollInputs() {
        while (true) {
            gamepad.poll();

            try {
                Component[] components = gamepad.getComponents();
                for (Component component : components) {
                    inputBuffer.put(component.getName(), component.getPollData());
                }
                Thread.sleep(10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
