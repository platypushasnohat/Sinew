package com.platypushasnohat.sinew.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record FreezeEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {

    public static final MapCodec<FreezeEffect> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(LevelBasedValue.CODEC.fieldOf("duration").forGetter(effect -> effect.duration)).apply(instance, FreezeEffect::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        int maxFreezeTicks = entity.getTicksRequiredToFreeze() * 2;
        entity.setTicksFrozen(Math.min(entity.getTicksFrozen() + Math.round(this.duration.calculate(enchantmentLevel)), maxFreezeTicks));
    }

    @Override
    public MapCodec<FreezeEffect> codec() {
        return CODEC;
    }
}
