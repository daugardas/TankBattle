package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.tankbattle.models.Entity;

public class RendererManager {
    private Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderers = new HashMap<>();

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
}
