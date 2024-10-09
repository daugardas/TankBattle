package com.tankbattle.server.strategies.Destructibility;

public class Destructible implements DestructibilityBehavior {
    @Override
    public boolean canBeDestroyed() {
        return true;
    }
}
