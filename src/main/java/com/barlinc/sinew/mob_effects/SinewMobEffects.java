package com.barlinc.sinew.mob_effects;

import com.barlinc.sinew.Sinew;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SinewMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Sinew.MOD_ID);

    public static final RegistryObject<MobEffect> ELECTRIFIED = MOB_EFFECTS.register("electrified", Electrified::new);
    public static final RegistryObject<MobEffect> STENCH = MOB_EFFECTS.register("stench", Stench::new);

}
