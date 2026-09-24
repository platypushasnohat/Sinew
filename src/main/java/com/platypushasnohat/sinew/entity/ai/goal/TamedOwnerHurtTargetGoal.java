package com.platypushasnohat.sinew.entity.ai.goal;

import com.platypushasnohat.sinew.entity.base.AnimatedTamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class TamedOwnerHurtTargetGoal extends TargetGoal {

    public final AnimatedTamableAnimal tameAnimal;
    public LivingEntity ownerLastHurt;
    public int timestamp;

    public TamedOwnerHurtTargetGoal(AnimatedTamableAnimal tameAnimal) {
        super(tameAnimal, false);
        this.tameAnimal = tameAnimal;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.tameAnimal.isTame() && this.tameAnimal.getCommand() != AnimatedTamableAnimal.COMMAND_SIT) {
            LivingEntity owner = this.tameAnimal.getOwner();
            if (owner == null) {
                return false;
            } else {
                this.ownerLastHurt = owner.getLastHurtMob();
                int timestamp = owner.getLastHurtMobTimestamp();
                return timestamp != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurt, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity owner = this.tameAnimal.getOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }
        super.start();
    }
}