package com.platypushasnohat.sinew.entity.base;

import com.platypushasnohat.sinew.client.animation.SmoothAnimationState;
import com.platypushasnohat.sinew.entity.ai.navigation.SmoothGroundNavigation;
import com.platypushasnohat.sinew.entity.utils.AnimatedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

public abstract class AnimatedTamableAnimal extends TamableAnimal implements AnimatedEntity {

    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AnimatedTamableAnimal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(AnimatedTamableAnimal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAME_ATTEMPTS = SynchedEntityData.defineId(AnimatedTamableAnimal.class, EntityDataSerializers.INT);

    public static final int COMMAND_SIT = 0;
    public static final int COMMAND_FOLLOW = 1;
    public static final int COMMAND_WANDER = 2;

    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();

    protected AnimatedTamableAnimal(EntityType<? extends AnimatedTamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_STATE, 0);
        builder.define(COMMAND, -1);
        builder.define(TAME_ATTEMPTS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Command", this.getCommand());
        compoundTag.putInt("TameAttempts", this.getTameAttempts());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setCommand(compoundTag.getInt("Command"));
        this.setTameAttempts(compoundTag.getInt("TameAttempts"));
    }

    @Override
    public int getAnimationState() {
        return this.getEntityData().get(ANIMATION_STATE);
    }

    @Override
    public void setAnimationState(int state) {
        this.getEntityData().set(ANIMATION_STATE, state);
    }

    public int getCommand() {
        return this.entityData.get(COMMAND);
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
    }

    public void setTameAttempts(int tameAttempts) {
        this.entityData.set(TAME_ATTEMPTS, tameAttempts);
    }

    public int getTameAttempts() {
        return this.entityData.get(TAME_ATTEMPTS);
    }

    public void tryToTame(Player player, ItemStack itemStack, int maxAttempts, int consumeAmount) {
        itemStack.consume(consumeAmount, player);
        this.setTameAttempts(this.getTameAttempts() + consumeAmount);
        if ((this.getTameAttempts() >= maxAttempts || player.isCreative()) && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.getNavigation().stop();
            this.setTarget(null);
            this.setCommand(COMMAND_SIT);
            this.setTameAttempts(0);
            player.displayClientMessage(Component.translatable("entity.sinew.all.command_" + this.getCommand(), this.getName()), true);
            this.level().broadcastEntityEvent(this, (byte) 7);
        } else {
            this.level().broadcastEntityEvent(this, (byte) 6);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean handleLeashAtDistance(Entity leashHolder, float distance) {
        if (this.getCommand() == COMMAND_SIT) {
            if (distance > 10.0F) {
                this.dropLeash(true, true);
            }
            return false;
        } else {
            return super.handleLeashAtDistance(leashHolder, distance);
        }
    }
}
