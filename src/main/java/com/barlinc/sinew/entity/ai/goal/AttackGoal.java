package com.barlinc.sinew.entity.ai.goal;

import com.barlinc.sinew.entity.utils.IAttackState;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class AttackGoal extends Goal {

    protected int timer = 0;
    protected final Mob mob;

    public AttackGoal(Mob mob) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.mob = mob;
    }

    @Override
    public void start() {
        this.mob.setAggressive(true);
        this.timer = 0;
        if (this.mob instanceof IAttackState animatedAttacker) {
            animatedAttacker.setAttackState(0);
        }
    }

    @Override
    public void stop() {
        LivingEntity target = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
        if (this.mob instanceof IAttackState animatedAttacker) {
            animatedAttacker.setAttackState(0);
        }
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.mob.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
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
}
