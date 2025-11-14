package com.unusualmodding.sinew.damage_types;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import static com.unusualmodding.sinew.Sinew.modPrefix;

public class SinewDamageTypes {

    public static final ResourceKey<DamageType> ELECTRIC = registerDamageType("electric");

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(ELECTRIC, new DamageType("electric", 0.0F));
    }

    public static ResourceKey<DamageType> registerDamageType(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, modPrefix(name));
    }

    public static DamageSource src(Level level, ResourceKey<DamageType> registerDamageType, Entity direct, Entity attacker) {
        var holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(registerDamageType);
        return new DamageSource(holder, direct, attacker);
    }

    public static DamageSource src(Level level, ResourceKey<DamageType> registerDamageType) {
        var holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(registerDamageType);
        return new DamageSource(holder);
    }
    
}