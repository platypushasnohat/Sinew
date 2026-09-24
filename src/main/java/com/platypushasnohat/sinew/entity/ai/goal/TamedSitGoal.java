package com.platypushasnohat.sinew.entity.ai.goal;

import com.platypushasnohat.sinew.entity.base.AnimatedTamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class TamedSitGoal extends Goal {

    public final AnimatedTamableAnimal mob;
    public final boolean shouldSitInWater;

    public TamedSitGoal(AnimatedTamableAnimal mob) {
        this(mob, false);
    }

    public TamedSitGoal(AnimatedTamableAnimal mob, boolean shouldSitInWater) {
        this.mob = mob;
        this.shouldSitInWater = shouldSitInWater;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getCommand() == AnimatedTamableAnimal.COMMAND_SIT;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        }
        else if (!this.shouldSitInWater && this.mob.isInWaterOrBubble()) {
            return false;
        }
        else if (!this.shouldSitInWater && !this.mob.onGround()) {
            return false;
        }
        else {
            LivingEntity owner = this.mob.getOwner();
            if (owner == null) {
                return true;
            } else {
                return (!(this.mob.distanceToSqr(owner) < 144.0) || owner.getLastHurtByMob() == null) && this.mob.getCommand() == AnimatedTamableAnimal.COMMAND_SIT;
            }
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setCommand(AnimatedTamableAnimal.COMMAND_SIT);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}
