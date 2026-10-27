package com.platypushasnohat.sinew.supporter;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.network.SupporterCapePacket;
import com.platypushasnohat.sinew.registry.SinewAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.UUID;

public class SupporterCapes {

    public static final ResourceLocation CAPE_TEXTURE = Sinew.location("textures/entity/barl_supporter_cape.png");

    public static boolean isSupporter(UUID uuid) {
        return getTier(uuid).isPresent();
    }

    public static Optional<SupporterTier> getTier(UUID uuid) {
        Optional<SupporterTier> tier = Optional.empty();
        if (SupporterList.DEVS.contains(uuid)) {
            tier = Optional.of(SupporterTier.DEV);
        }
        else if (SupporterList.SUPPORTERS.contains(uuid)) {
            tier = Optional.of(SupporterTier.SUPPORTER);
        }
        return tier;
    }

    public static boolean isEnabled(ServerPlayer player) {
        return player.getData(SinewAttachments.SUPPORTER_CAPE.get());
    }

    public static void setEnabled(ServerPlayer player, boolean enabled) {
        boolean allowed = enabled && isSupporter(player.getUUID());
        player.setData(SinewAttachments.SUPPORTER_CAPE.get(), allowed);
        PacketDistributor.sendToPlayersTrackingEntity(player, new SupporterCapePacket(player.getUUID(), allowed));
    }

    @EventBusSubscriber(modid = Sinew.MOD_ID)
    public static class Events {

        @SubscribeEvent
        public static void onStartTracking(PlayerEvent.StartTracking event) {
            if (event.getTarget() instanceof ServerPlayer target && event.getEntity() instanceof ServerPlayer tracker) {
                PacketDistributor.sendToPlayer(tracker, new SupporterCapePacket(target.getUUID(), isEnabled(target)));
            }
        }
    }
}
