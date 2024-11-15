package com.tankbattle.server.models.tanks.weaponsystems;

import com.tankbattle.server.utils.Vector2;

public abstract class WeaponSystem {
    protected int cooldownTime;
    protected long lastFireTime;

    public WeaponSystem (int cooldownTime, long lastFireTime){
        this.cooldownTime = cooldownTime;
        this.lastFireTime = lastFireTime;
    }

    public abstract void fire(Vector2 location, Vector2 direction);

    public boolean canFire() {
        return System.currentTimeMillis() - lastFireTime >= cooldownTime;
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }
}
