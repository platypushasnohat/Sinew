package com.platypushasnohat.sinew.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SwimWanderGoal extends RandomSwimmingGoal {

    public final int radius;
    public final int height;
    public int stuckChecks;
    public Vec3 lastPos = Vec3.ZERO;

    public SwimWanderGoal(PathfinderMob mob, double speedModifier, int interval) {
        this(mob, speedModifier, interval, 10, 7);
    }

    public SwimWanderGoal(PathfinderMob mob, double speedModifier, int interval, int radius, int height) {
        super(mob, speedModifier, interval);
        this.radius = radius;
        this.height = height;
    }

    @Override
    public void start() {
        super.start();
        this.stuckChecks = 0;
        this.lastPos = this.mob.position();
    }

    @Override
    public boolean canContinueToUse() {
        Vec3 position = this.mob.position();
        this.stuckChecks = position.distanceToSqr(this.lastPos) < 0.0025D ? this.stuckChecks + 1 : 0;
        this.lastPos = position;
        if (this.stuckChecks > 40) {
            return false;
        }
        return super.canContinueToUse() && this.mob.distanceToSqr(this.wantedX, this.wantedY, this.wantedZ) > 30;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);
    }
}