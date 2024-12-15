package com.tankbattle.server.models.tanks;

import com.tankbattle.server.models.tanks.weaponsystems.Cannon;
import com.tankbattle.server.utils.Vector2;

public class HeavyTank extends Tank {
    public HeavyTank() {
        super(new Cannon(), new Vector2(800, 800), 150, 35);
    }
}
