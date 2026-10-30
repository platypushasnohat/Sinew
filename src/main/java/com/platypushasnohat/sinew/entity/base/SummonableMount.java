package com.platypushasnohat.sinew.entity.base;

import com.platypushasnohat.sinew.entity.utils.KeybindUsingMount;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class SummonableMount extends SummonableMonster implements KeybindUsingMount {

    public static final int RIDER_ATTACK = 0;

    protected SummonableMount(EntityType<? extends SummonableMount> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onSummoned(Player owner) {
        super.onSummoned(owner);
        owner.startRiding(this, true);
    }

    @Override
    public float getSummonCost() {
        return 0.0F;
    }

    public boolean hasRiderAttack() {
        return false;
    }

    public void onRiderAttack(Player rider) {
    }

    public boolean acceptsRiderKey(int type) {
        return type == RIDER_ATTACK && this.hasRiderAttack();
    }

    public void onRiderKey(Player rider, int type) {
        if (type == RIDER_ATTACK) {
            this.onRiderAttack(rider);
        }
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser instanceof Player rider && rider == this.getControllingPassenger() && this.acceptsRiderKey(type)) {
            this.onRiderKey(rider, type);
        }
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return this.isSummoned() && this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger();
    }

    @Override
    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float strafe = player.xxa * 0.5F;
        float forward = player.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        return new Vec3(strafe, 0.0D, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getControllingPassenger() != null) {
            this.getNavigation().stop();
            this.setTarget(null);
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide && this.isSummoned() && !this.isRemoved() && passenger.getUUID().equals(this.getOwnerUUID())) {
            this.expire();
        }
    }
}
