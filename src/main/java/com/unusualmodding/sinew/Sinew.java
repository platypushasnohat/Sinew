package com.unusualmodding.sinew;

import com.unusualmodding.sinew.setup.ClientSetup;
import com.unusualmodding.sinew.setup.ModSetup;
import com.unusualmodding.sinew.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

@Mod(Sinew.MODID)
public class Sinew {

    public static final String MODID = "sinew";
    public static final Logger LOGGER = LogManager.getLogger();

    public Sinew() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        Registration.init();
        ModSetup.setup();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);

        if(FMLEnvironment.dist == Dist.CLIENT) {
            modbus.addListener(ClientSetup::init);
        }



    }

    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}

