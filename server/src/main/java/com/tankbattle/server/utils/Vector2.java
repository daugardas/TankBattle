package com.tankbattle.server.utils;

public class Vector2 {
    private float x;
    private float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        if (x % 1 >= 0.5) {
            return (int) x + 1;
        } else {
            return (int) x;
        }
    }

    public int getY() {
        if (y % 1 >= 0.5) {
            return (int) y + 1;
        } else {
            return (int) y;
        }
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void addToX(float x){
        this.x += x;
    }

    public void addToY(float y){
        this.y += y;
    }
}
