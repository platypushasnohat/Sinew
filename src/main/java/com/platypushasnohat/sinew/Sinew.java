package com.platypushasnohat.sinew;

import com.mojang.logging.LogUtils;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.network.ActionBarPacket;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import com.platypushasnohat.sinew.network.MultipartEntityPacket;
import com.platypushasnohat.sinew.network.ParticlePacket;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import com.platypushasnohat.sinew.registry.SinewDataComponents;
import com.platypushasnohat.sinew.registry.SinewEnchantmentEffects;
import com.platypushasnohat.sinew.utils.SinewClientProxy;
import com.platypushasnohat.sinew.utils.SinewCommonProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public static final SinewCommonProxy PROXY = Sinew.unsafeRunForDist(() -> SinewClientProxy::new, () -> SinewCommonProxy::new);

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path.toLowerCase(Locale.ROOT));
    }

    public Sinew(IEventBus modEventBus, ModContainer modContainer) {
        SinewAttributes.ATTRIBUTES.register(modEventBus);
        SinewDataComponents.DATA_COMPONENTS.register(modEventBus);
        SinewEnchantmentEffects.ENTITY_EFFECTS.register(modEventBus);
        SinewEnchantmentEffects.LOCATION_EFFECTS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::packetSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, SinewConfig.COMMON_CONFIG, "sinew-common.toml");
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (SinewConfig.RESISTANCE_USES_DAMAGE_REDUCTION.get()) {
                MobEffects.DAMAGE_RESISTANCE.value().addAttributeModifier(SinewAttributes.DAMAGE_REDUCTION, location("effect.resistance"), 0.2D, AttributeModifier.Operation.ADD_VALUE);
            }
        });
    }

    public void packetSetup(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID).versioned("1.0.0").optional();
        registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.CODEC, ParticlePacket::handle);
        registrar.playToClient(ActionBarPacket.TYPE, ActionBarPacket.CODEC, ActionBarPacket::handle);

        registrar.playToServer(MultipartEntityPacket.TYPE, MultipartEntityPacket.CODEC, MultipartEntityPacket::handle);
        registrar.playToServer(MountedEntityKeyPacket.TYPE, MountedEntityKeyPacket.CODEC, MountedEntityKeyPacket::handle);
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> clientTarget.get().get();
            case DEDICATED_SERVER -> serverTarget.get().get();
        };
    }
}
