package com.tankbattle.utils;

public class Vector2 {
    private float x;
    private float y;
    private static final float SCALE = 2.0f; // Interesting interaction with server side, needs to be looked at later.
                                             // pasol

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getScaledX() {
        return Math.round(x * SCALE);
    }

    public int getScaledY() {
        return Math.round(y * SCALE);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addToX(float x) {
        this.x += x;
    }

    public void addToY(float y) {
        this.y += y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}