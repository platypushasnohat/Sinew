package com.platypushasnohat.sinew.entity.ai.navigation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SmoothGroundNavigation extends GroundPathNavigation implements ExtendedNavigator {

    public SmoothGroundNavigation(Mob mob, Level level) {
        super(mob, level);
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
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return this.createSmoothPathFinder(nodeEvaluator, maxVisitedNodes);
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    protected void followThePath() {
        final Vec3 safeSurfacePos = this.getTempMobPos();
        final int shortcutNode = this.getClosestVerticalTraversal(Mth.floor(safeSurfacePos.y));
        this.maxDistanceToWaypoint = mob.getBbWidth() > 0.75F ? mob.getBbWidth() / 2.0F : 0.75F - mob.getBbWidth() / 2.0F;
        if (!this.attemptShortcut(shortcutNode, safeSurfacePos)) {
            if (this.isCloseToNextNode(0.5F) || this.isAboutToTraverseVertically() && this.isCloseToNextNode(getMaxDistanceToWaypoint())) {
                this.path.advance();
            }
        }
        this.doStuckDetection(safeSurfacePos);
    }

    @Override
    public int getSurfaceY() {
        return super.getSurfaceY();
    }

    @SuppressWarnings("DataFlowIssue")
    protected int getClosestVerticalTraversal(int safeSurfaceHeight) {
        final int nodesLength = path.getNodeCount();
        for (int nodeIndex = path.getNextNodeIndex(); nodeIndex < nodesLength; nodeIndex++) {
            if (path.getNode(nodeIndex).y != safeSurfaceHeight) {
                return nodeIndex;
            }
        }
        return nodesLength;
    }
}