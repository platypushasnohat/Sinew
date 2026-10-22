package com.platypushasnohat.sinew.registry;

import com.mojang.serialization.MapCodec;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.loot.ReplaceTableLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SinewLootModifierTypes {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Sinew.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceTableLootModifier>> REPLACE_TABLE = LOOT_MODIFIERS.register("replace_table", () -> ReplaceTableLootModifier.CODEC);
}
