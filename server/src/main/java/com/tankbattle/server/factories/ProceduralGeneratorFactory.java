package com.tankbattle.server.factories;

import com.tankbattle.server.strategies.Level.ProceduralGenerator;
import com.tankbattle.server.strategies.Level.RandomPlacementGenerator;

public class ProceduralGeneratorFactory {
    public static ProceduralGenerator createGenerator(String type) {
        switch (type) {
            case "random":
                return new RandomPlacementGenerator(40, 50);

            default:
                throw new IllegalArgumentException("Unknown generator type");
        }
    }
}
