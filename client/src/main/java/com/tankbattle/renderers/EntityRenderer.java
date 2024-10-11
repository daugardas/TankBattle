package com.tankbattle.renderers;

import java.awt.Graphics2D;

import com.tankbattle.models.Entity;
import com.tankbattle.utils.Vector2;

public interface EntityRenderer<T extends Entity> {
    void draw(Graphics2D g2d, T entity);

    void setWorldOffset(Vector2 worldOffset);
}
