package com.platypushasnohat.sinew.api.entity.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

public class SmoothWallClimberNavigation extends WallClimberNavigation implements ExtendedNavigator {

    public SmoothWallClimberNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public Mob getMob() {
        return mob;
    }

    @Override
    @Nullable
    public Path getPath() {
        return super.getPath();
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return this.createSmoothPathFinder(nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public int getSurfaceY() {
        return super.getSurfaceY();
    }
}