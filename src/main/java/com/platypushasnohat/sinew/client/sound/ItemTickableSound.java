package com.platypushasnohat.sinew.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public abstract class ItemTickableSound extends AbstractTickableSoundInstance {

    protected final LivingEntity living;

    public ItemTickableSound(LivingEntity living, SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.living = living;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.x = this.living.getX();
        this.y = this.living.getY();
        this.z = this.living.getZ();
        this.delay = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.living.isSilent() && this.living.isUsingItem() && (this.isValidItem(this.living.getItemInHand(InteractionHand.MAIN_HAND)) || this.isValidItem(this.living.getItemInHand(InteractionHand.OFF_HAND)));
    }

    @Override
    public void tick() {
        ItemStack itemStack = ItemStack.EMPTY;
        if (this.living.isUsingItem()) {
            if (isValidItem(this.living.getItemInHand(InteractionHand.MAIN_HAND))) {
                itemStack = this.living.getItemInHand(InteractionHand.MAIN_HAND);
            }
            if (isValidItem(this.living.getItemInHand(InteractionHand.OFF_HAND))) {
                itemStack = this.living.getItemInHand(InteractionHand.OFF_HAND);
            }
        }
        if (this.living.isAlive() && !itemStack.isEmpty()) {
            this.x = this.living.getX();
            this.y = this.living.getY();
            this.z = this.living.getZ();
            this.tickVolume(itemStack);
        } else {
            this.stop();
        }
    }

    protected abstract void tickVolume(ItemStack itemStack);

    public abstract boolean isValidItem(ItemStack itemStack);

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(LivingEntity living) {
        return this.living.isAlive() && this.living.getId() == living.getId();
    }
}