package com.tankbattle.server.models.tanks;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HealthBoostDecorator extends TankDecorator {
    @JsonIgnore
    private int extraHealth;
    @JsonIgnore
    private long startTime;
    @JsonIgnore
    private long duration;
    @JsonIgnore
    private int initialHealth;
    @JsonIgnore boolean expired = false;

    public HealthBoostDecorator(ITank decoratedTank, int extraHealth, long duration) {
        super(decoratedTank);
        this.extraHealth = extraHealth;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.initialHealth = decoratedTank.getHealth();

        decoratedTank.setHealth(decoratedTank.getHealth() + extraHealth);
    }
    
    @Override
    public void updateLocation() {
        if (isExpired()) {
            int currentHealth = super.getHealth();
            if (currentHealth > initialHealth) {
                super.setHealth(initialHealth);
            }
        }
        super.updateLocation();
    }
    
    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;

    }
}
