package com.tankbattle.server.models.tanks.weaponsystems;

import java.util.Timer;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.utils.Vector2;

public class MachineGun extends WeaponSystem {
    private final static int COOLDOWN_TIME = 600;
    private final static int RESET_THRESHOLD = 800;

    public MachineGun() {
        super(COOLDOWN_TIME, 0);
    }

    @Override
    public void fire(Vector2 location, Vector2 direction) {
        updateCooldown();

        if (canFire()) {
            Bullet bullet = new Bullet(location, direction, new Vector2(32, 32), 60, 60);
            GameController gameController = SpringContext.getBean(GameController.class);
            gameController.addBullet(bullet);
            lastFireTime = System.currentTimeMillis();

            if (cooldownTime >= 300) {
                setCooldownTime(cooldownTime - 100);
            }

        }
    }

    private void updateCooldown() {
        if (System.currentTimeMillis() - lastFireTime >= RESET_THRESHOLD) {
            resetCooldown();
        }
    }

    private void resetCooldown() {
        cooldownTime = COOLDOWN_TIME;
    }
}
