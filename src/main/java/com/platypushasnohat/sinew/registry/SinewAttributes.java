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

    public static final DeferredHolder<Attribute, Attribute> PROJECTILE_DAMAGE = register("projectile_damage", 0.0D, 0.0D, 2048.0D);
    public static final DeferredHolder<Attribute, Attribute> AIR_SPEED = registerPercentage("air_speed", 0.0D, 0.0D, 30.0D);
    public static final DeferredHolder<Attribute, Attribute> EXPERIENCE_BOOST = registerPercentage("experience_boost", 0.0D, 0.0D, 30.0D);
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = register("magic_damage", 0.0D, 0.0D, 2048.0D);
    public static final DeferredHolder<Attribute, Attribute> DODGE_CHANCE = registerPercentage("dodge_chance", 0.0D, 0.0D, 1.0D);
    public static final DeferredHolder<Attribute, Attribute> LIFE_STEAL = registerPercentage("life_steal", 0.0D, 0.0D, 1024.0D);
    public static final DeferredHolder<Attribute, Attribute> ARMOR_PIERCING = registerPercentage("armor_piercing", 0.0D, 0.0D, 1.0D);
    public static final DeferredHolder<Attribute, Attribute> DAMAGE_REDUCTION = registerPercentage("damage_reduction", 0.0D, 0.0D, 1.0D);
    public static final DeferredHolder<Attribute, Attribute> FISHING_LURE = register("fishing_lure", 0.0D, 0.0D, 1024.0D);
    public static final DeferredHolder<Attribute, Attribute> FISHING_LUCK = register("fishing_luck", 0.0D, 0.0D, 1024.0D);
    public static final DeferredHolder<Attribute, Attribute> FORTUNE = register("fortune", 0.0D, 0.0D, 1024.0D);
    public static final DeferredHolder<Attribute, Attribute> LOOTING = register("looting", 0.0D, 0.0D, 1024.0D);
    public static final DeferredHolder<Attribute, Attribute> AMMO_SAVE_CHANCE = registerPercentage("ammo_save_chance", 0.0D, 0.0D, 1.0D);
    public static final DeferredHolder<Attribute, Attribute> MANA_CONSERVE_CHANCE = registerPercentage("mana_conserve_chance", 0.0D, 0.0D, 1.0D);

    @SuppressWarnings("SameParameterValue")
    private static DeferredHolder<Attribute, Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + Sinew.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue).setSyncable(true));
    }

    @SuppressWarnings("SameParameterValue")
    private static DeferredHolder<Attribute, Attribute> registerPercentage(String name, double defaultValue, double minimumValue, double maximumValue) {
        return ATTRIBUTES.register(name, () -> new PercentageAttribute("attribute." + Sinew.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue).setSyncable(true));
    }
}
