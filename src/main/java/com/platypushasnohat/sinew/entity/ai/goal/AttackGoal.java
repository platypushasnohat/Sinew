package com.platypushasnohat.sinew.entity.ai.goal;

import com.platypushasnohat.sinew.entity.utils.AnimatedEntity;
import com.platypushasnohat.sinew.utils.SinewMiscUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

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
        if (this.mob instanceof AnimatedEntity animated) {
            animated.setAnimationState(0);
        }
        this.timer = 0;
        this.attackState = 0;
    }

    @Override
    public void stop() {
        this.mob.setPose(Pose.STANDING);
        this.mob.setTarget(null);
        this.mob.setAggressive(false);
        if (this.mob instanceof AnimatedEntity animated) {
            animated.setAnimationState(0);
        }
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
        return this.getAttackReachSqr(target, 2.0D);
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

    public Vec3 rotateOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(-xRot * Mth.DEG_TO_RAD).yRot(-yRot * Mth.DEG_TO_RAD);
    }

    public boolean isWithinYRange(LivingEntity target, int range) {
        if (target == null) {
            return false;
        }
        return Math.abs(target.getY() - this.mob.getY()) < range;
    }

    public boolean isInAttackBox(LivingEntity target, double lookScale, double width, double height) {
        return this.isInAttackBox(target, lookScale, width, height, true, false);
    }

    public boolean isInAttackBox(LivingEntity target, double lookScale, double width, double height, boolean scaleLookY) {
        return this.isInAttackBox(target, lookScale, width, height, scaleLookY, false);
    }

    public boolean isInAttackBox(LivingEntity target, double lookScale, double width, double height, boolean scaleLookY, boolean debug) {
        AABB attackBox = this.mob.getBoundingBox().move(this.mob.getLookAngle().normalize().multiply(lookScale, scaleLookY ? lookScale : 0.0D, lookScale)).inflate(width, height, width);
        List<LivingEntity> nearbyEntities = this.mob.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), this.mob, attackBox);
        if (debug) {
            SinewMiscUtils.outlineBounds(attackBox, target.level(), ParticleTypes.ELECTRIC_SPARK);
        }
        return this.mob.hasLineOfSight(target) && !nearbyEntities.isEmpty() && nearbyEntities.contains(target);
    }
}
