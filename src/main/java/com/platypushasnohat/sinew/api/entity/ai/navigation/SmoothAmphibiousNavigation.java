package com.platypushasnohat.sinew.api.entity.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;

import javax.annotation.Nullable;

public class SmoothAmphibiousNavigation extends AmphibiousPathNavigation implements ExtendedNavigator {

    public SmoothAmphibiousNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    public boolean prefersShallowSwimming() {
        return false;
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
        this.nodeEvaluator = new AmphibiousNodeEvaluator(this.prefersShallowSwimming());
        this.nodeEvaluator.setCanPassDoors(true);
        return this.createSmoothPathFinder(nodeEvaluator, maxVisitedNodes);
    }
}