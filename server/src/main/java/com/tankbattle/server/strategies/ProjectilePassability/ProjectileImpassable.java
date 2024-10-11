package com.tankbattle.server.strategies.ProjectilePassability;

public class ProjectileImpassable implements ProjectilePassabilityBehaviour {
    @Override
    public boolean canProjectilePass() {
        return false;
    }
}
