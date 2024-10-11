package com.tankbattle.server.strategies.Passability;

public class Impassable implements PassabilityBehavior {
    @Override
    public boolean canPass() {
        return false;
    }
}
