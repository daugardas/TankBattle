package com.tankbattle.server.utils;

public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Add two vectors
    public Vector2 add(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    // Subtract two vectors
    public Vector2 subtract(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    // Multiply vector by scalar
    public Vector2 multiply(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    // Calculate the magnitude of the vector
    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    // Normalize the vector
    public Vector2 normalize() {
        float mag = magnitude();
        if (mag == 0)
            return new Vector2(0, 0);
        return new Vector2(this.x / mag, this.y / mag);
    }

    // Dot product
    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    public String toString() {
        return String.format("{ x: %f, y: %f }", this.x, this.y);
    }

    // Other useful methods like cross product, rotate, etc., can be added here.
}
