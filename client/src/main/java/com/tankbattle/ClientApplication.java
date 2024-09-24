package com.tankbattle;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.views.GameWindow;

public class ClientApplication {
    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow();
        GameManager.getInstance().addGameWindow(gameWindow);
    }
}
