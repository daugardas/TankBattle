package com.tankbattle.server.factories;

import com.tankbattle.server.iterators.LineByLineIterator;
import com.tankbattle.server.iterators.SpiralIterator;
import com.tankbattle.server.strategies.Level.LevelGenerator;
import com.tankbattle.server.strategies.Level.PrebuiltLevelGenerator;
import com.tankbattle.server.strategies.Level.RandomPlacementGenerator;

public class LevelGeneratorFactory {
    public static LevelGenerator createGenerator(String type) {
        switch (type) {
            case "random":
                return new RandomPlacementGenerator(40, 10, new SpiralIterator(null));
            case "random-line":
                return new RandomPlacementGenerator(40, 10, new LineByLineIterator(null));
            case "prebuilt":
                return new PrebuiltLevelGenerator();
            default:
                throw new IllegalArgumentException("Unknown generator type");
        }
    }
}
