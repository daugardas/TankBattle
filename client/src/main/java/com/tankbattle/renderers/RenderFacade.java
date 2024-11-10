package com.tankbattle.renderers;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.models.Bullet;
import com.tankbattle.models.Collision;
import com.tankbattle.models.Entity;
import com.tankbattle.models.Player;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RenderFacade {
    private final Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderers = new ConcurrentHashMap<>();
    private double scaleFactor;
    private double worldLocationScaleFactor;
    private Vector2 worldOffset;

    public RenderFacade(ResourceManager resourceManager) {
        // Register all renderers
        registerRenderer(Player.class, new TankRenderer(resourceManager));
        registerRenderer(Tile.class, new TileRenderer(resourceManager));
        registerRenderer(Collision.class, new ExplosionRenderer(resourceManager));
        registerRenderer(Bullet.class, new BulletRenderer(resourceManager));
    }

    public <T extends Entity> void registerRenderer(Class<T> entityClass, EntityRenderer<T> renderer) {
        renderers.put(entityClass, renderer);
    }

    @SuppressWarnings("unchecked")
    public void drawEntity(Graphics2D g2d, Entity entity) {
        Class<?> clazz = entity.getClass();
        EntityRenderer renderer = null;

        // Locate renderer for the entity's class or its superclass
        while (clazz != null && renderer == null) {
            renderer = renderers.get(clazz);
            clazz = clazz.getSuperclass();
        }

        if (renderer != null) {
            renderer.draw(g2d, entity);
        }
    }

    public <T extends Entity> void drawEntities(Graphics2D g2d, List<T> entities) {
        for (T entity : entities) {
            drawEntity(g2d, entity);
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
