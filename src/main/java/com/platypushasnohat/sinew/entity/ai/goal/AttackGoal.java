package com.platypushasnohat.sinew.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class AttackGoal extends Goal {

    protected final PathfinderMob mob;
    protected int timer = 0;
    protected int attackState;

    public AttackGoal(PathfinderMob mob) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.mob = mob;
    }

    @Override
    public void start() {
        this.mob.setPose(Pose.STANDING);
        this.mob.setAggressive(true);
        this.timer = 0;
        this.attackState = 0;
    }

    @Override
    public void stop() {
        this.mob.setPose(Pose.STANDING);
        this.mob.setTarget(null);
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
        this.timer = 0;
        this.attackState = 0;
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive() && !this.mob.isVehicle();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return false;
        }
        else if (!target.isAlive()) {
            return false;
        }
        else if (!mob.isWithinRestriction(target.blockPosition())) {
            return false;
        }
        else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative() || !this.mob.getNavigation().isDone();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
    }

    protected double getAttackReachSqr(LivingEntity target, double distance) {
        return this.mob.getBbWidth() * distance * this.mob.getBbWidth() * distance + target.getBbWidth();
    }

    protected boolean isInAttackRange(LivingEntity target, double reach) {
        return this.mob.hasLineOfSight(target) && this.mob.distanceTo(target) < this.mob.getBbWidth() + target.getBbWidth() + reach;
    }

    protected void lookAtTarget(LivingEntity target, float yaw, float pitch) {
        this.mob.getLookControl().setLookAt(target, yaw, pitch);
        this.mob.lookAt(target, yaw, pitch);
    }
}
