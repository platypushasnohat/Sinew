package com.platypushasnohat.sinew.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class AquaticPanicGoal extends PanicGoal {

    public int timeout;

    public AquaticPanicGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    public void start() {
        super.start();
        this.timeout = 0;
    }

    @Override
    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        } else {
            return this.findRandomPosition();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.timeout < 40 && super.canContinueToUse();
    }

    @Override
    public void tick() {
        this.timeout++;
    }

    @Override
    protected boolean findRandomPosition() {
        Vec3 randomPos = BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7);
        if (this.mob.getLastHurtByMob() != null) {
            randomPos = DefaultRandomPos.getPosAway(this.mob, 10, 7, this.mob.getLastHurtByMob().position());
        }
        if (randomPos == null) {
            return false;
        }
        else {
            this.posX = randomPos.x;
            this.posY = randomPos.y;
            this.posZ = randomPos.z;
            return true;
        }
    }
}
