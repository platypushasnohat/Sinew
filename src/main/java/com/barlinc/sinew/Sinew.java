package com.barlinc.sinew;

import com.barlinc.sinew.setup.ClientSetup;
import com.barlinc.sinew.setup.ModSetup;
import com.barlinc.sinew.setup.Registration;
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

@Mod(Sinew.MOD_ID)
public class Sinew {

    public static final String MOD_ID = "sinew";
    public static final Logger LOGGER = LogManager.getLogger();

    public Sinew() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init();
        ModSetup.setup();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modbus.addListener(ClientSetup::init);
        }
    }

    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }
}

