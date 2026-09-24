package com.platypushasnohat.sinew.entity.ai.goal;

import com.platypushasnohat.sinew.entity.base.AnimatedTamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class TamedOwnerHurtByTargetGoal extends TargetGoal {

    public final AnimatedTamableAnimal tameAnimal;
    public LivingEntity ownerLastHurtBy;
    public int timestamp;

    public TamedOwnerHurtByTargetGoal(AnimatedTamableAnimal tameAnimal) {
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
                this.ownerLastHurtBy = owner.getLastHurtByMob();
                int timestamp = owner.getLastHurtByMobTimestamp();
                return timestamp != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity owner = this.tameAnimal.getOwner();
        if (owner != null) {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}