package com.tankbattle.models;

import com.tankbattle.utils.Vector2;

public class Bullet extends Entity {
    public Bullet(Vector2 location, Vector2 size){
        this.location = location;
        this.size = (int)size.getX();
    }
}
