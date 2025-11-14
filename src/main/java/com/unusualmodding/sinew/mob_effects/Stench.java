package com.unusualmodding.sinew.mob_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Stench extends MobEffect {

    public Stench() {
        super(MobEffectCategory.HARMFUL, 0x0080c3);
    }



    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}