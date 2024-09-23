package com.tankbattle.utils;

public class Vector2 {
    public int x;
    public int y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    public Vector2 subtract(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    public Vector2 multiply(int scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public int magnitude() {
        return (int) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2 normalize() {
        int mag = magnitude();
        if (mag == 0) return new Vector2(0, 0);
        return new Vector2(this.x / mag, this.y / mag);
    }

    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }
}
