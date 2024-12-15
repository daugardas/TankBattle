package com.tankbattle.server.models.tiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.components.SpringContext;
import com.tankbattle.server.controllers.GameController;
import com.tankbattle.server.strategies.Destructibility.DestructibilityBehavior;
import com.tankbattle.server.strategies.Passability.PassabilityBehavior;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectilePassabilityBehaviour;

public abstract class Tile implements Cloneable {
    private PassabilityBehavior passabilityBehavior;
    private ProjectilePassabilityBehaviour projectilePassabilityBehaviour;
    private DestructibilityBehavior destructibilityBehavior;

    private int health;

    public Tile(PassabilityBehavior passability, ProjectilePassabilityBehaviour projectilePassability,
            DestructibilityBehavior destructibility) {
        passabilityBehavior = passability;
        projectilePassabilityBehaviour = projectilePassability;
        destructibilityBehavior = destructibility;

        health = 300;
    }

    @JsonIgnore
    public boolean canPass() {
        return passabilityBehavior.canPass();
    }

    @JsonIgnore
    public boolean canProjectilePass() {
        return projectilePassabilityBehaviour.canProjectilePass();
    }

    @JsonIgnore
    public boolean canBeDestroyed() {
        return destructibilityBehavior.canBeDestroyed();
    }

    public void takeDamage(int damage) {
        if (this.canBeDestroyed()) {
            health -= damage;
        }
    }

    public Tile copyShallow() {
        try {
            return (Tile) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tile copyDeep() {
        Tile deepCopy = new DestructibleTile();
        deepCopy.setHealth(this.getHealth());
        return deepCopy;
    }

    @JsonIgnore
    public void setPassabilityBehavior(PassabilityBehavior behavior) {
        passabilityBehavior = behavior;
    }

    @JsonIgnore
    public void setProjectilePassabilityBehavior(ProjectilePassabilityBehaviour behavior) {
        projectilePassabilityBehaviour = behavior;
    }

    @JsonIgnore
    public void setDestructibilityBehavior(DestructibilityBehavior behavior) {
        destructibilityBehavior = behavior;
    }

    @JsonIgnore
    public abstract void interact();

    @JsonIgnore
    public abstract String getSymbol();

    public String getType() {
        return this.getClass().getSimpleName();
    };

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
