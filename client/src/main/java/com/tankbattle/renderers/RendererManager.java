package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.tankbattle.models.Entity;
import com.tankbattle.utils.Vector2;

public class RendererManager {
    private Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderers = new HashMap<>();
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;

    public <T extends Entity> void registerRenderer(Class<T> entityClass, EntityRenderer<T> renderer) {
        renderers.put(entityClass, renderer);
    }

    @SuppressWarnings("unchecked")
    public void draw(Graphics2D g2d, Entity entity) {
        Class<?> clazz = entity.getClass();
        EntityRenderer renderer = null;

        while (clazz != null && renderer == null) {
            renderer = renderers.get(clazz);
            clazz = clazz.getSuperclass();
        }

        if (renderer != null) {
            renderer.draw(g2d, entity);
        }
    }

    public void setRenderingScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            if (renderer instanceof Scalable) {
                ((Scalable) renderer).setRenderingScaleFactor(scaleFactor);
            }
        }
    }

    public void setWorldLocationScaleFactor(double worldLocationScaleFactor) {
        this.worldLocationScaleFactor = worldLocationScaleFactor;
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            if (renderer instanceof Scalable) {
                ((Scalable) renderer).setWorldLocationScaleFactor(worldLocationScaleFactor);
            }
        }
    }

    public void setWorldOffset(Vector2 worldOffset) {
        this.worldOffset = worldOffset;
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            renderer.setWorldOffset(worldOffset);
        }
    }
}
