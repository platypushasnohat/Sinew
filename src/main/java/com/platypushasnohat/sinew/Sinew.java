package com.platypushasnohat.sinew;

import com.platypushasnohat.sinew.api.network.ParticlePacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Locale;

@Mod(Sinew.MOD_ID)
public class Sinew {

    public static final String MOD_ID = "sinew";

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path.toLowerCase(Locale.ROOT));
    }

    public Sinew(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::packetSetup);
    }

    public void packetSetup(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("1.0.0").optional();
        registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.CODEC, ParticlePacket::handle);
    }
}
