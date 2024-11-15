package com.tankbattle.server.models.tanks;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SpeedBoostDecorator extends TankDecorator {
    @JsonIgnore
    private int extraSpeed;
    @JsonIgnore
    private long startTime;
    @JsonIgnore
    private long duration; // in milliseconds
    @JsonIgnore
    private int initialSpeed;

    public SpeedBoostDecorator(ITank decoratedTank, int extraSpeed, long duration) {
        super(decoratedTank);
        this.extraSpeed = extraSpeed;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.initialSpeed = decoratedTank.getSpeed();
    }
    
    @Override
    public void updateLocation() {
        if (!isExpired()) {
            super.setSpeed(initialSpeed + extraSpeed);
        } else {
            super.setSpeed(initialSpeed);
        }
        super.updateLocation();
    }

    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;
    }
}

