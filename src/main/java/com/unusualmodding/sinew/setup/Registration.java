package com.unusualmodding.sinew.setup;

import com.unusualmodding.sinew.attributes.SinewAttributes;
import com.unusualmodding.sinew.mob_effects.SinewMobEffects;
import com.unusualmodding.sinew.registry.SinewParticleTypes;
import com.unusualmodding.sinew.world.SinewFeatures;
import com.unusualmodding.sinew.world.SinewRuleTests;
import com.unusualmodding.sinew.world.SinewStructureProcessors;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Registration {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init(){

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SinewParticleTypes.PARTICLE_TYPES.register(bus);
        SinewMobEffects.MOB_EFFECTS.register(bus);
        SinewAttributes.ATTRIBUTES.register(bus);
        SinewRuleTests.RULE_TESTS.register(bus);
        SinewFeatures.FEATURES.register(bus);
        SinewStructureProcessors.STRUCTURE_PROCESSOR_TYPE.register(bus);
    }
}