package com.tankbattle.renderers;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tankbattle.controllers.ResourceManager;
import com.tankbattle.factories.TileFactory;
import com.tankbattle.models.Bullet;
import com.tankbattle.models.Collision;
import com.tankbattle.models.Entity;
import com.tankbattle.models.Player;
import com.tankbattle.models.PowerUp;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.utils.Vector2;

public class RenderFacade {
    private final Map<Class<? extends Entity>, EntityRenderer<? extends Entity>> renderers = new ConcurrentHashMap<>();

    private final TileFactory tileFactory;

    public RenderFacade(ResourceManager resourceManager) {
        this.tileFactory = new TileFactory(resourceManager);

        // Register all renderers
        registerRenderer(Player.class, new TankRenderer(resourceManager));
        registerRenderer(Tile.class, new TileRenderer());
        registerRenderer(Collision.class, new ExplosionRenderer(resourceManager));
        registerRenderer(Bullet.class, new BulletRenderer(resourceManager));
        registerRenderer(PowerUp.class, new PowerUpRenderer(resourceManager));
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
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            if (renderer != null) {
                ((Scalable) renderer).setRenderingScaleFactor(scaleFactor);
            }
        }
    }

    public void setWorldLocationScaleFactor(double worldLocationScaleFactor) {
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            if (renderer != null) {
                ((Scalable) renderer).setWorldLocationScaleFactor(worldLocationScaleFactor);
            }
        }
    }

    public void setWorldOffset(Vector2 worldOffset) {
        for (EntityRenderer<? extends Entity> renderer : renderers.values()) {
            renderer.setWorldOffset(worldOffset);
        }
    }

    public TileFactory getTileFactory() {
        return tileFactory;
    }
}
