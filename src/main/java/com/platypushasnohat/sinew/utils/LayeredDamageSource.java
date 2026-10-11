package com.platypushasnohat.sinew.utils;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LayeredDamageSource extends DamageSource {

    private final DamageSource originalSource;

    public LayeredDamageSource(Holder.Reference<DamageType> message, DamageSource originalSource) {
        super(message);
        this.originalSource = originalSource;
    }

    public LayeredDamageSource(Holder.Reference<DamageType> message, Entity source, DamageSource originalSource) {
        super(message, source);
        this.originalSource = originalSource;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity attacked) {
        return this.originalSource.getLocalizedDeathMessage(attacked);
    }
}