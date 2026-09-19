package com.platypushasnohat.sinew.registry;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SinewAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Sinew.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> RANGED_DAMAGE = register("ranged_damage", 0.0D, -2048.0D, 2048.0D);

    private static DeferredHolder<Attribute, Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + Sinew.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
    }

    private static DeferredHolder<Attribute, Attribute> registerPercentage(String name, double defaultValue, double minimumValue, double maximumValue) {
        return ATTRIBUTES.register(name, () -> new PercentageAttribute("attribute." + Sinew.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue));
    }
}
