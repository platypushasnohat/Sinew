package com.platypushasnohat.sinew.api.entity.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;

import javax.annotation.Nullable;

public class SmoothWaterNavigation extends WaterBoundPathNavigation implements ExtendedNavigator {

    protected final boolean canBreach;

    public SmoothWaterNavigation(Mob mob, Level level) {
        this(mob, level, false);
    }

    public SmoothWaterNavigation(Mob mob, Level level, boolean canBreach) {
        super(mob, level);
        this.canBreach = canBreach;
    }

    @Override
    protected boolean canUpdatePath() {
        return canBreach || mob.isInLiquid();
    }

    @Override
    public Mob getMob() {
        return mob;
    }

    @Nullable
    @Override
    public Path getPath() {
        return super.getPath();
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new SwimNodeEvaluator(canBreach);
        this.nodeEvaluator.setCanPassDoors(true);
        return this.createSmoothPathFinder(nodeEvaluator, maxVisitedNodes);
    }
}
