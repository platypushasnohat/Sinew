package com.platypushasnohat.sinew.tags;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public class SinewMobEffectTags {

    public static final TagKey<MobEffect> IMMUNE_TO_DURATION_REDUCTION = modMobEffectTag("immune_to_duration_reduction");

    private static TagKey<MobEffect> modMobEffectTag(String name) {
        return mobEffectTag(Sinew.MOD_ID, name);
    }

    public static TagKey<MobEffect> mobEffectTag(String modid, String name) {
        return TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(modid, name));
    }
}
