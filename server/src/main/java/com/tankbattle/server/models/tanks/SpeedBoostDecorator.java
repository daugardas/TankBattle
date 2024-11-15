package com.tankbattle.server.models.tanks;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SpeedBoostDecorator extends TankDecorator {
    @JsonIgnore
    private int extraSpeed;
    @JsonIgnore
    private long startTime;
    @JsonIgnore
    private long duration; // in milliseconds

    public SpeedBoostDecorator(ITank decoratedTank, int extraSpeed, long duration) {
        super(decoratedTank);
        this.extraSpeed = extraSpeed;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }

    
    @Override
    public int getSpeed() {
        if (!isExpired()) {
            return decoratedTank.getSpeed() + extraSpeed;
        } else {
            return decoratedTank.getSpeed()+ extraSpeed;
        }
    }

    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;
    }
}

