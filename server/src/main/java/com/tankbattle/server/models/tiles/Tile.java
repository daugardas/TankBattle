package com.tankbattle.server.models.tiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tankbattle.server.strategies.Destructibility.DestructibilityBehavior;
import com.tankbattle.server.strategies.Passability.PassabilityBehavior;
import com.tankbattle.server.strategies.ProjectilePassability.ProjectilePassabilityBehaviour;

public abstract class Tile {
    private PassabilityBehavior passabilityBehavior;
    private ProjectilePassabilityBehaviour projectilePassabilityBehaviour;
    private DestructibilityBehavior destructibilityBehavior;

    public Tile(PassabilityBehavior passability, ProjectilePassabilityBehaviour projectilePassability,
            DestructibilityBehavior destructibility) {
        passabilityBehavior = passability;
        projectilePassabilityBehaviour = projectilePassability;
        destructibilityBehavior = destructibility;
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

    public String getType(){
        return this.getClass().getSimpleName();
    };
}
