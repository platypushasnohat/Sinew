package com.platypushasnohat.sinew.registry;

import com.mojang.serialization.MapCodec;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.enchantment.FreezeEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SinewEnchantmentEffects {

    public static final DeferredRegister<MapCodec<? extends EnchantmentLocationBasedEffect>> LOCATION_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, Sinew.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Sinew.MOD_ID);

    public static final DeferredHolder<?, ?> FREEZE_LOCATION = LOCATION_EFFECTS.register("freeze", () -> FreezeEffect.CODEC);
    public static final DeferredHolder<?, ?> FREEZE_ENTITY = ENTITY_EFFECTS.register("freeze", () -> FreezeEffect.CODEC);

}
