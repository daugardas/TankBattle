package com.tankbattle.server.strategies.Destructibility;

public class Indestructible implements DestructibilityBehavior {
    @Override
    public boolean canBeDestroyed() {
        return false;
    }
}
