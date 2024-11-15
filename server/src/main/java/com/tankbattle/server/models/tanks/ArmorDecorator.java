package com.tankbattle.server.models.tanks;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ArmorDecorator extends TankDecorator {
    @JsonIgnore
    private int damageReduction; // Amount to reduce from incoming damage
    @JsonIgnore
    private long startTime;
    @JsonIgnore
    private long duration; // in milliseconds

    public ArmorDecorator(ITank decoratedTank, int damageReduction, long duration) {
        super(decoratedTank);
        this.damageReduction = damageReduction;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }
    
    @Override
    public void takeDamage(int damage) {
        if (!isExpired()) {
            damage = Math.max(0, damage - damageReduction);
        }
        super.takeDamage(damage);
    }
    
    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;
    }
}
