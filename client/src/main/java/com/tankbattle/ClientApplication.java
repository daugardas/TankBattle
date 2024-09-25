package com.tankbattle;

import java.util.Scanner;

import com.tankbattle.controllers.GameManager;
import com.tankbattle.views.GameWindow;

public class ClientApplication {
    public static void main(String[] args) {
        GameManager.getInstance().initialize();
    }
}
