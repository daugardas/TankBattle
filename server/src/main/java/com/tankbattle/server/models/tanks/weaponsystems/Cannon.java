package com.tankbattle.server.models.tanks.weaponsystems;

import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.Bullet;
import com.tankbattle.server.models.Player;
import com.tankbattle.server.utils.Vector2;

public class Cannon extends WeaponSystem {
    private final static int COOLDOWN_TIME = 1000;

    public Cannon() {
        super(COOLDOWN_TIME, 0);
    }

    @Override
    public void fire(Vector2 location, Vector2 direction, Player shooter) {
        if (canFire()) {
            Bullet bullet = new Bullet(location, direction, new Vector2(48, 48), 50, 50, shooter);
            GameController gameController = SpringContext.getBean(GameController.class);
            gameController.addBullet(bullet);
            lastFireTime = System.currentTimeMillis();
        }
    }
}
