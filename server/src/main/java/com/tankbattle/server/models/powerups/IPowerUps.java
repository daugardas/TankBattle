package com.tankbattle.server.models.powerups;

public interface IPowerUps {

    float getSpeed();
    int getHealth();
    void updateLocation();
    void takeDamage(int damage);

}
