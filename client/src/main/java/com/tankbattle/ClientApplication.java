package com.tankbattle;

import com.tankbattle.controllers.GameManager;

public class ClientApplication {
    public static void main(String[] args) {
        GameManager.getInstance().initialize();
    }
}