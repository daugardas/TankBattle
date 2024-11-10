package com.tankbattle.renderers;

import com.tankbattle.models.Entity;
import com.tankbattle.utils.Vector2;

import java.awt.*;

public interface EntityRenderer<T extends Entity> {
    void draw(Graphics2D g2d, T entity);

    void setWorldOffset(Vector2 worldOffset);
}
