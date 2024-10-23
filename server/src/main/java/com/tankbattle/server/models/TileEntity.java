package com.tankbattle.server.models;

import java.util.ArrayList;
import java.util.List;

import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public class TileEntity implements GameEntity {
    private Tile tile;
    private int gridX;
    private int gridY;

    // For spatial grid management
    private int[] cellIndicesMin;
    private int[] cellIndicesMax;
    private List<GridNode> gridNodes = new ArrayList<>();
    private boolean isStaticEntity = true; // Tiles are static by default
    private int queryId;

    public TileEntity(Tile tile, int gridX, int gridY) {
        this.tile = tile;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    @Override
    public Vector2 getLocation() {
        float x = gridX * GameController.TILE_WIDTH + GameController.TILE_WIDTH / 2.0f;
        float y = gridY * GameController.TILE_HEIGHT + GameController.TILE_HEIGHT / 2.0f;
        return new Vector2(x, y);
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(GameController.TILE_WIDTH, GameController.TILE_HEIGHT);
    }

    @Override
    public int getQueryId() {
        return queryId;
    }

    @Override
    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    @Override
    public void setCellIndices(int[] minIndices, int[] maxIndices) {
        this.cellIndicesMin = minIndices;
        this.cellIndicesMax = maxIndices;
    }

    @Override
    public int[] getCellIndicesMin() {
        return cellIndicesMin;
    }

    @Override
    public int[] getCellIndicesMax() {
        return cellIndicesMax;
    }

    @Override
    public void addGridNode(GridNode node) {
        gridNodes.add(node);
    }

    @Override
    public List<GridNode> getGridNodes() {
        return gridNodes;
    }

    @Override
    public void clearGridNodes() {
        gridNodes.clear();
    }

    @Override
    public void setStaticEntity(boolean isStatic) {
        this.isStaticEntity = isStatic;
    }

    @Override
    public boolean isStaticEntity() {
        return isStaticEntity;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean canPass() {
        return tile.canPass();
    }
}
