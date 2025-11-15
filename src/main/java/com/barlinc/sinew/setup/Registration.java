package com.barlinc.sinew.setup;

import com.barlinc.sinew.attributes.SinewAttributes;
import com.barlinc.sinew.mob_effects.SinewMobEffects;
import com.barlinc.sinew.registry.SinewParticleTypes;
import com.barlinc.sinew.world.SinewFeatures;
import com.barlinc.sinew.world.SinewRuleTests;
import com.barlinc.sinew.world.SinewStructureProcessors;
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