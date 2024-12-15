package com.tankbattle.server.models.tanks;

import com.tankbattle.server.models.tanks.weaponsystems.MachineGun;
import com.tankbattle.server.utils.Vector2;

public class LightTank extends Tank {
    public LightTank() {
        super(new MachineGun(), new Vector2(800, 800), 100, 30);
    }
}
