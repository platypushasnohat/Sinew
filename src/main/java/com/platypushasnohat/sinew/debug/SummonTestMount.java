package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.entity.base.SummonableMount;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SummonTestMount extends SummonableMount {

    private int riderAttackCooldown;

    public SummonTestMount(EntityType<? extends SummonTestMount> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.riderAttackCooldown > 0) {
            this.riderAttackCooldown--;
        }
    }

    @Override
    public boolean hasRiderAttack() {
        return true;
    }

    @Override
    public void onRiderAttack(Player rider) {
        if (this.riderAttackCooldown <= 0) {
            AABB area = this.getBoundingBox().inflate(1.0D).move(this.getLookAngle().scale(1.5D));
            for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, area, entity -> entity != this && entity != rider && entity.isAlive() && !this.isAlliedTo(entity))) {
                this.doHurtTarget(target);
            }
            this.swing(InteractionHand.MAIN_HAND);
            this.riderAttackCooldown = 10;
        }
    }

    @Override
    public void setupAnimationStates() {
    }
}
