package com.platypushasnohat.sinew.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SinewSoundUtils {

    public static float randomizePitch(LivingEntity entity) {
        return 1.0F / (entity.getRandom().nextFloat() * 0.4F + 0.8F);
    }

    public static float randomizePitch(Level level) {
        return 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
    }
}
