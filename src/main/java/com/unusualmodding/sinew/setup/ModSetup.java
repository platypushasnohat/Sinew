package com.unusualmodding.sinew.setup;

import com.unusualmodding.sinew.Sinew;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Sinew.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event){
    }

    public static void setup(){
        IEventBus bus = MinecraftForge.EVENT_BUS;
    }


}