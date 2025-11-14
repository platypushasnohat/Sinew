package com.unusualmodding.sinew.setup;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.heart_type.SinewHeartTypes;
import com.unusualmodding.sinew.managers.ScreenshakeManager;
import com.unusualmodding.sinew.utils.ClientUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Sinew.MODID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(FMLClientSetupEvent event){
        event.enqueueWork(SinewHeartTypes::register);
    }





}