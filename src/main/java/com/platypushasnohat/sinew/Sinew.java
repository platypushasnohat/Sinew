package com.platypushasnohat.sinew;

import com.mojang.logging.LogUtils;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.network.ActionBarPacket;
import com.platypushasnohat.sinew.network.ParticlePacket;
import com.platypushasnohat.sinew.registry.SinewSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import java.util.Locale;
import java.util.function.Supplier;

@Mod(Sinew.MOD_ID)
public class Sinew {

    public static final String MOD_ID = "sinew";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path.toLowerCase(Locale.ROOT));
    }

    public Sinew(IEventBus modEventBus, ModContainer modContainer) {
        SinewSoundEvents.SOUND_EVENTS.register(modEventBus);
        modEventBus.addListener(this::packetSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, SinewConfig.COMMON_CONFIG, "sinew-common.toml");
    }

    public void packetSetup(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("1.0.0").optional();
        registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.CODEC, ParticlePacket::handle);
        registrar.playToClient(ActionBarPacket.TYPE, ActionBarPacket.CODEC, ActionBarPacket::handle);
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> clientTarget.get().get();
            case DEDICATED_SERVER -> serverTarget.get().get();
        };
    }
}
