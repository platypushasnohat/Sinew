package com.platypushasnohat.sinew.utils;

import com.platypushasnohat.sinew.client.particle.AfterImageParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SinewParticleUtils {

    public static void createAfterImage(LivingEntity entity, Vec3 vector) {
        Minecraft.getInstance().particleEngine.add(new AfterImageParticle(entity, (ClientLevel) entity.level(), entity.xOld + (vector.x / 1.5D), entity.yOld, entity.zOld + (vector.z / 1.5D)));
    }
}
