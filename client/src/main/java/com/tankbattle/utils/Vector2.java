package com.tankbattle.utils;

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

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public Vector2 subtract(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public Vector2 multiply(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2 normalize() {
        float mag = magnitude();
        if (mag == 0)
            return new Vector2(0, 0);
        return new Vector2(this.x / mag, this.y / mag);
    }

    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }
}
