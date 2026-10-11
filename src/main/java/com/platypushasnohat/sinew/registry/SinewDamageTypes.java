package com.platypushasnohat.sinew.registry;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.utils.LayeredDamageSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class SinewDamageTypes {

    public static final ResourceKey<DamageType> RANGED_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Sinew.location("ranged"));

    public static DamageSource causeRangedDamage(RegistryAccess registryAccess, Entity source, DamageSource original) {
        return new LayeredDamageSource(registryAccess.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RANGED_DAMAGE), source, original);
    }
}
