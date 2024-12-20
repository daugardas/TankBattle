package com.tankbattle.visitors;

import com.tankbattle.models.Bullet;
import com.tankbattle.models.Collision;
import com.tankbattle.models.Player;
import com.tankbattle.models.PowerUp;
import com.tankbattle.models.tiles.Tile;
import com.tankbattle.renderers.RenderFacade;

import java.awt.Graphics2D;

public class RenderVisitor implements Visitor {
    private final RenderFacade renderFacade;
    private final Graphics2D graphics;

    public RenderVisitor(RenderFacade renderFacade, Graphics2D graphics) {
        this.renderFacade = renderFacade;
        this.graphics = graphics;
    }

    @Override
    public void visit(Bullet bullet) {
        renderFacade.drawEntity(graphics, bullet);
    }

    @Override
    public void visit(Collision collision) {
        renderFacade.drawEntity(graphics, collision);
    }

    @Override
    public void visit(Player player) {
        renderFacade.drawEntity(graphics, player);
    }

    @Override
    public void visit(PowerUp powerUp) {
        renderFacade.drawEntity(graphics, powerUp);
    }

    @Override
    public void visit(Tile tile) {
        renderFacade.drawEntity(graphics, tile);
    }
}