package com.platypushasnohat.sinew.utils;

import com.platypushasnohat.sinew.client.particle.AfterImageParticle;
import com.platypushasnohat.sinew.network.ParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SinewParticleUtils {

    public static void createAfterImage(LivingEntity entity, Vec3 vector) {
        Minecraft.getInstance().particleEngine.add(new AfterImageParticle(entity, (ClientLevel) entity.level(), entity.xOld + (vector.x / 1.5D), entity.yOld, entity.zOld + (vector.z / 1.5D)));
    }

    public static void queueParticlesOnBlockFaces(ServerLevel level, BlockPos pos, ParticleOptions particle, IntProvider count) {
        for (Direction direction : Direction.values()) {
            queueParticlesOnBlockFace(level, pos, particle, count, direction, () -> getRandomSpeedRanges(level.random), 0.55D);
        }
    }

    public static void queueParticlesOnBlockFace(ServerLevel level, BlockPos pos, ParticleOptions particleOptions, IntProvider count, Direction direction, Supplier<Vec3> supplier, double v) {
        int sample = count.sample(level.random);
        for (int j = 0; j < sample; j++) {
            queueParticleOnFace(level, pos, direction, particleOptions, supplier.get(), v);
        }
    }

    public static void queueParticleOnFace(ServerLevel level, BlockPos pos, Direction direction, ParticleOptions particle, Vec3 speed, double v) {
        ParticlePacket particlePacket = new ParticlePacket();
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int stepX = direction.getStepX();
        int stepY = direction.getStepY();
        int stepZ = direction.getStepZ();
        double x = vec3.x + (stepX == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepX * v);
        double y = vec3.y + (stepY == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepY * v);
        double z = vec3.z + (stepZ == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepZ * v);
        double xSpeed = stepX == 0 ? speed.x() : 0.0D;
        double ySpeed = stepY == 0 ? speed.y() : 0.0D;
        double zSpeed = stepZ == 0 ? speed.z() : 0.0D;
        particlePacket.queueParticle(particle, x, y, z, xSpeed, ySpeed, zSpeed);
        PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), particlePacket);
    }

    public static Vec3 getRandomSpeedRanges(RandomSource random) {
        return new Vec3(Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D));
    }
}
