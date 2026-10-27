package com.platypushasnohat.sinew.supporter;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.network.SupporterCapePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT)
public class SupporterCapesClient {

    private static final Set<UUID> ENABLED_PLAYERS = ConcurrentHashMap.newKeySet();

    private static final String TOGGLE_FILE = "sinew_supporter_cape.txt";

    private static Boolean enabled;

    public static boolean shouldShowCape(Player player) {
        boolean enabled = player.isLocalPlayer() ? isEnabled() : ENABLED_PLAYERS.contains(player.getUUID());
        return enabled && SupporterCapes.isSupporter(player.getUUID());
    }

    public static boolean isLocalSupporter() {
        return SupporterCapes.isSupporter(Minecraft.getInstance().getUser().getProfileId());
    }

    public static boolean isEnabled() {
        if (enabled == null) {
            enabled = loadEnabled();
        }
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
        try {
            Files.writeString(getToggleFile(), Boolean.toString(value), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            Sinew.LOGGER.error("Failed to save supporter cape toggle", exception);
        }
        sendToServer();
    }

    private static boolean loadEnabled() {
        boolean value = false;
        Path file = getToggleFile();
        if (Files.exists(file)) {
            try {
                value = Boolean.parseBoolean(Files.readString(file, StandardCharsets.UTF_8).trim());
            }
            catch (IOException exception) {
                Sinew.LOGGER.error("Failed to read supporter cape toggle", exception);
            }
        }
        return value;
    }

    private static Path getToggleFile() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve(TOGGLE_FILE);
    }

    public static void setOtherEnabled(UUID player, boolean enabled) {
        if (enabled) {
            ENABLED_PLAYERS.add(player);
        }
        else {
            ENABLED_PLAYERS.remove(player);
        }
    }

    private static void sendToServer() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null && connection.hasChannel(SupporterCapePacket.TYPE) && isLocalSupporter()) {
            PacketDistributor.sendToServer(new SupporterCapePacket(Minecraft.getInstance().getUser().getProfileId(), isEnabled()));
        }
    }

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        sendToServer();
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ENABLED_PLAYERS.clear();
    }
}
