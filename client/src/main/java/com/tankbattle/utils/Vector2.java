package com.tankbattle.utils;
public class Vector2 {
    private float x;
    private float y;
    private static final float SCALE = 2.0f;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        if (x % 1 != 0) {
            return (int) x + 1;
        } else {
            return (int) x;
        }
    }

    public int getY() {
        if (y % 1 != 0) {
            return (int) y + 1;
        } else {
            return (int) y;
        }
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

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 addVector(Vector2 other) {
        x += other.getX();
        y += other.getY();
        return new Vector2(x, y);
    }

    public Vector2 addFloat(float number) {
        x += number;
        y += number;

        return new Vector2(x, y);
    }

    public void addToX(float x) {
        this.x += x;
    }

    public void addToY(float y) {
        this.y += y;
    }

    public Vector2 multiply(float other) {
        this.x *= other;
        this.y *= other;

        return new Vector2(x, y);
    }

    public float manhattanDistance(Vector2 other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public double distanceTo(Vector2 other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public static Vector2 convertByteToVector2(byte data) {
        Vector2 result = new Vector2();

        if ((data >> 3 << 3 & Constants.DIRECTION_UP) != 0) {
            result.setY(-1);
        }

        if ((data >> 1 << 3 >> 2 & Constants.DIRECTION_DOWN) != 0) {
            result.setY(1);
        }

        if ((data << 1 >> 3 << 2 & Constants.DIRECTION_LEFT) != 0) {
            result.setX(-1);
        }

        if ((data << 3 >> 3 & Constants.DIRECTION_RIGHT) != 0) {
            result.setX(1);
        }

        return result;
    }

    public boolean equals(Vector2 other) {
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

