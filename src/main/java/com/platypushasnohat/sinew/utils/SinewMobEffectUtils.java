package com.platypushasnohat.sinew.utils;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SinewMobEffectUtils {

    public static void amplifyEffect(MobEffectInstance instance, LivingEntity target, int addedAmplifier, int cap) {
        instance.amplifier = Math.clamp(cap, instance.getAmplifier(), instance.getAmplifier() + addedAmplifier);
        syncEffect(instance, target);
    }

    public static void amplifyEffect(MobEffectInstance instance, LivingEntity target, int addedAmplifier) {
        instance.amplifier = instance.getAmplifier() + addedAmplifier;
        syncEffect(instance, target);
    }

    public static void extendEffect(MobEffectInstance instance, LivingEntity target, int addedDuration, int cap) {
        instance.duration = Math.clamp(cap, instance.getDuration(), instance.getDuration() + addedDuration);
        syncEffect(instance, target);
    }

    public static void extendEffect(MobEffectInstance instance, LivingEntity target, int addedDuration) {
        instance.duration = instance.getDuration() + addedDuration;
        syncEffect(instance, target);
    }

    public static void shortenEffect(MobEffectInstance instance, LivingEntity target, int removedDuration) {
        instance.duration = instance.getDuration() - removedDuration;
        syncEffect(instance, target);
    }

    public static void syncEffect(MobEffectInstance instance, LivingEntity target) {
        target.effectsDirty = true;
        target.onEffectUpdated(instance, true, target);
    }
}
