package com.tankbattle.server.models.tanks;

import java.awt.Color;

import com.tankbattle.server.models.tanks.weaponsystems.Cannon;
import com.tankbattle.server.models.tanks.weaponsystems.MachineGun;
import com.tankbattle.server.utils.Vector2;

public class HeavyTank extends Tank {
    public HeavyTank() {
        super(new MachineGun(), new Vector2(800, 800), 200, 30);
    }
}
