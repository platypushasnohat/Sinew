package com.platypushasnohat.sinew.utils;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RandomMessageDamageSource extends DamageSource {

    private final int messageCount;

    public RandomMessageDamageSource(Holder.Reference<DamageType> message, int messageCount) {
        super(message);
        this.messageCount = messageCount;
    }

    public RandomMessageDamageSource(Holder.Reference<DamageType> message, Entity source, int messageCount) {
        super(message, source);
        this.messageCount = messageCount;
    }

    public RandomMessageDamageSource(Holder.Reference<DamageType> message, Entity directEntity, Entity causingEntity, int messageCount) {
        super(message, directEntity, causingEntity);
        this.messageCount = messageCount;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity attacked) {
        int type = attacked.getRandom().nextInt(this.messageCount);
        String string = "death.attack." + this.getMsgId() + "_" + type;
        Entity killer = this.getEntity() == null ? this.getDirectEntity() : this.getEntity();
        if (killer != null) {
            return Component.translatable(string + ".entity", attacked.getDisplayName(), killer.getDisplayName());
        } else {
            return Component.translatable(string, attacked.getDisplayName());
        }
    }
}