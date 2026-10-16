package com.platypushasnohat.sinew.tags;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class SinewDamageTypeTags {

    public static final TagKey<DamageType> IS_LAYERED = modDamageTag("is_layered");
    public static final TagKey<DamageType> TRIGGERS_MAGIC_DAMAGE = modDamageTag("triggers_magic_damage");
    public static final TagKey<DamageType> IGNORES_ATTACK_COOLDOWN_SCALING = modDamageTag("ignores_attack_cooldown_scaling");
    public static final TagKey<DamageType> BYPASSES_DODGE = modDamageTag("bypasses_dodge");
    public static final TagKey<DamageType> DOES_NOT_LIFESTEAL = modDamageTag("does_not_lifesteal");

    private static TagKey<DamageType> modDamageTag(String name) {
        return damageTag(Sinew.MOD_ID, name);
    }

    private static TagKey<DamageType> commonDamageTag(String name) {
        return damageTag("c", name);
    }

    public static TagKey<DamageType> damageTag(String modId, String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name));
    }
}
