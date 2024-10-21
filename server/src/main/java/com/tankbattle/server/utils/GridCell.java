package com.tankbattle.server.utils;

import java.util.Objects;

/**
 * Represents a grid cell in the SpatialGrid.
 * Immutable class to ensure thread safety and consistency.
 */
public final class GridCell {
    private final int x;
    private final int y;

    /**
     * Constructs a GridCell with specified x and y coordinates.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     */
    public GridCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the cell.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the cell.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Overrides the equals method for proper comparison.
     *
     * @param o The object to compare.
     * @return True if both GridCells have the same coordinates, else false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridCell gridCell = (GridCell) o;
        return x == gridCell.x && y == gridCell.y;
    }

    /**
     * Overrides the hashCode method for proper hashing in collections.
     *
     * @return The hash code based on x and y coordinates.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
