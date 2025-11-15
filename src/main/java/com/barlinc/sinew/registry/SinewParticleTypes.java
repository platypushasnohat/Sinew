package com.barlinc.sinew.registry;

import com.barlinc.sinew.Sinew;
import com.barlinc.sinew.particles.LightningParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SinewParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Sinew.MOD_ID);

    public static final RegistryObject<LightningParticleType> LIGHTNING = PARTICLE_TYPES.register("lightning", () -> new LightningParticleType(false));
}
