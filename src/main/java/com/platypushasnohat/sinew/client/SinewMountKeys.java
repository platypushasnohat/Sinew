package com.platypushasnohat.sinew.client;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.base.SummonableMount;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT)
public class SinewMountKeys {

    private static final Map<KeyMapping, Integer> KEYS = new ConcurrentHashMap<>();

    public static void bind(KeyMapping key, int type) {
        KEYS.put(key, type);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getVehicle() instanceof SummonableMount mount && mount.getControllingPassenger() == player) {
            KEYS.forEach((key, type) -> {
                while (key.consumeClick()) {
                    if (mount.acceptsRiderKey(type)) {
                        PacketDistributor.sendToServer(new MountedEntityKeyPacket(mount.getId(), player.getId(), type));
                    }
                }
            });
        }
    }
}
