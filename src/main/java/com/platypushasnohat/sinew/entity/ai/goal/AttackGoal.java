package com.platypushasnohat.sinew.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class AttackGoal extends Goal {

    public final PathfinderMob mob;
    public int timer = 0;
    public int attackState;

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

    public double getAttackReachSqr(LivingEntity target) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
    }

    public double getAttackReachSqr(LivingEntity target, double distance) {
        return this.mob.getBbWidth() * distance * this.mob.getBbWidth() * distance + target.getBbWidth();
    }

    public boolean isInAttackRange(LivingEntity target, double reach) {
        return this.mob.hasLineOfSight(target) && this.mob.distanceTo(target) < this.mob.getBbWidth() + target.getBbWidth() + reach;
    }

    public void lookAtTarget(LivingEntity target, float yaw, float pitch) {
        this.mob.getLookControl().setLookAt(target, yaw, pitch);
        this.mob.lookAt(target, yaw, pitch);
    }

    public void faceVec(Vec3 pos, float yawConstraint, float pitchConstraint) {
        double xOffset = pos.x() - this.mob.getX();
        double zOffset = pos.z() - this.mob.getZ();
        double yOffset = this.mob.getY() + (double) 0.25F - pos.y();
        double distance = Mth.sqrt((float) (xOffset * xOffset + zOffset * zOffset));
        float xyAngle = (float) (Math.atan2(zOffset, xOffset) * (double) 180.0F / Math.PI) - 90.0F;
        float zdAngle = (float) (-(Math.atan2(yOffset, distance) * (double) 180.0F / Math.PI));
        this.mob.setXRot(-this.updateRotation(this.mob.getXRot(), zdAngle, pitchConstraint));
        this.mob.setYRot(this.updateRotation(this.mob.getYRot(), xyAngle, yawConstraint));
    }

    public float updateRotation(float current, float target, float maxDelta) {
        float delta = Mth.wrapDegrees(target - current);
        if (delta > maxDelta) {
            delta = maxDelta;
        }
        if (delta < -maxDelta) {
            delta = -maxDelta;
        }
        return current + delta;
    }
}
