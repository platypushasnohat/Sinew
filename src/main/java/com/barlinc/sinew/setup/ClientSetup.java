package com.barlinc.sinew.setup;

import com.barlinc.sinew.Sinew;
import com.barlinc.sinew.heart_type.SinewHeartTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(FMLClientSetupEvent event){
        event.enqueueWork(SinewHeartTypes::register);
    }

}