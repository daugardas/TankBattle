package com.tankbattle.server.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.models.tiles.Tile;
import com.tankbattle.server.utils.SpatialGrid.GridNode;
import com.tankbattle.server.utils.Vector2;

public class TileEntity implements GameEntity {
    private Tile tile;
    private int gridX;
    private int gridY;

    // For spatial grid management
    @JsonIgnore
    private int queryId;
    @JsonIgnore
    private int[] cellIndicesMin;
    @JsonIgnore
    private int[] cellIndicesMax;
    @JsonIgnore
    private List<GridNode> gridNodes = new ArrayList<>();
    @JsonIgnore
    private boolean isStaticEntity = false;
    @JsonIgnore
    private Set<String> occupiedCells = new HashSet<>();

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

    public boolean canProjectilePass() {
        return tile.canProjectilePass();
    }

    public void takeDamage(int damage) {
        tile.takeDamage(damage);
    }

        @Override
    public Set<String> getOccupiedCellKeys() {
       return new HashSet<>(occupiedCells);
    }

    @Override
    public void setOccupiedCells(Set<String> occupiedCells) {
        this.occupiedCells = (occupiedCells != null) ? new HashSet<>(occupiedCells) : new HashSet<>();
    }
    
}
