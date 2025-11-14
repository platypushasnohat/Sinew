package com.unusualmodding.sinew.registry;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.particles.LightningParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SinewParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Sinew.MODID);

    public static final RegistryObject<LightningParticleType> LIGHTNING = PARTICLE_TYPES.register("lightning", () -> new LightningParticleType(false));
}
