package com.platypushasnohat.sinew.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SinewMiscUtils {

    public static void outlineBounds(AABB aabb, Level level, ParticleOptions particle) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            outlineBounds(aabb, serverLevel, particle);
        }
    }

    public static void outlineBounds(AABB aabb, ServerLevel level, ParticleOptions particle) {
        level.sendParticles(particle, aabb.maxX, aabb.maxY, aabb.maxZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.maxX, aabb.minY, aabb.minZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.maxX, aabb.minY, aabb.maxZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.maxX, aabb.maxY, aabb.minZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.minX, aabb.maxY, aabb.maxZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.minX, aabb.minY, aabb.minZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.minX, aabb.minY, aabb.maxZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.sendParticles(particle, aabb.minX, aabb.maxY, aabb.minZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }
}
