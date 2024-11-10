package com.tankbattle;

import com.tankbattle.controllers.GameManager;

public class ClientApplication {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        GameManager.getInstance().initialize();
    }
}