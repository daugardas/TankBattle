package com.tankbattle.server.models.tiles;

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

    public boolean canPass() {
        return passabilityBehavior.canPass();
    }

    public boolean canProjectilePass() {
        return projectilePassabilityBehaviour.canProjectilePass();
    }

    public boolean canBeDestroyed() {
        return destructibilityBehavior.canBeDestroyed();
    }

    public void setPassabilityBehavior(PassabilityBehavior behavior) {
        passabilityBehavior = behavior;
    }

    public void setProjectilePassabilityBehavior(ProjectilePassabilityBehaviour behavior) {
        projectilePassabilityBehaviour = behavior;
    }

    public void setDestructibilityBehavior(DestructibilityBehavior behavior) {
        destructibilityBehavior = behavior;
    }

    public abstract void interact();

    public abstract String getSymbol();
}
