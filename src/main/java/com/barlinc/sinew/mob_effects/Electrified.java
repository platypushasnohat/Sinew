package com.barlinc.sinew.mob_effects;

import com.barlinc.sinew.damage_types.SinewDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Electrified extends MobEffect {

    public Electrified() {
        super(MobEffectCategory.HARMFUL, 0x0080c3);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.isInWaterRainOrBubble()) {
            entity.hurt(entity.damageSources().source(SinewDamageTypes.ELECTRIC), 2.0F + (amplifier * 1.5F));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}