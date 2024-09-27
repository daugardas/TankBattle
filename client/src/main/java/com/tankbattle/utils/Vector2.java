package com.tankbattle.utils;

public class Vector2 {
    private float x;
    private float y;
    private static final float SCALE = 2.0f; // Interesting interaction with server side, needs to be looked at later

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        int scaledX = Math.round(x * SCALE);
        if (scaledX % 1 >= 0.5) {
            return scaledX + 1;
        } else {
            return scaledX;
        }
    }

    public int getY() {
        int scaledY = Math.round(y * SCALE);
        if (scaledY % 1 >= 0.5) {
            return scaledY + 1;
        } else {
            return scaledY;
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
