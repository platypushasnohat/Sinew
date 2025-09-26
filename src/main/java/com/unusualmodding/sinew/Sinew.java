package com.unusualmodding.sinew;

import com.unusualmodding.sinew.network.SinewNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

@Mod(Sinew.MODID)
public class Sinew {

    public static final String MODID = "sinew";
    public static final Logger LOGGER = LogManager.getLogger();

    public Sinew() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        SinewNetwork.register();
    }

    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}

