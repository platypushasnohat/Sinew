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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT)
public class SupporterCapesClient {

    private static final Map<UUID, SupporterCape> PLAYER_CAPES = new ConcurrentHashMap<>();

    private static final String TOGGLE_FILE = "sinew_supporter_cape.txt";

    private static SupporterCape selected;

    public static SupporterCape getShownCape(Player player) {
        SupporterCape cape = player.isLocalPlayer() ? getSelected() : PLAYER_CAPES.getOrDefault(player.getUUID(), SupporterCape.NONE);
        return cape.canUse(player.getUUID()) ? cape : SupporterCape.NONE;
    }

    public static UUID getLocalUUID() {
        return Minecraft.getInstance().getUser().getProfileId();
    }

    public static List<SupporterCape> getAvailable() {
        return SupporterCape.getAvailable(getLocalUUID());
    }

    public static SupporterCape getSelected() {
        if (selected == null) {
            selected = loadSelected();
        }
        return selected;
    }

    public static void setSelected(SupporterCape cape) {
        selected = cape;
        try {
            Files.writeString(getToggleFile(), cape.getSerializedName(), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            Sinew.LOGGER.error("Failed to save supporter cape", exception);
        }
        sendToServer();
    }

    private static SupporterCape loadSelected() {
        SupporterCape cape = SupporterCape.NONE;
        Path file = getToggleFile();
        if (Files.exists(file)) {
            try {
                cape = SupporterCape.byName(Files.readString(file, StandardCharsets.UTF_8).trim());
            }
            catch (IOException exception) {
                Sinew.LOGGER.error("Failed to read supporter cape", exception);
            }
        }
        return cape;
    }

    private static Path getToggleFile() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve(TOGGLE_FILE);
    }

    public static void setOtherCape(UUID player, SupporterCape cape) {
        PLAYER_CAPES.put(player, cape);
    }

    private static void sendToServer() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null && connection.hasChannel(SupporterCapePacket.TYPE) && SupporterCapes.isSupporter(getLocalUUID())) {
            PacketDistributor.sendToServer(new SupporterCapePacket(getLocalUUID(), getSelected()));
        }
    }

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        sendToServer();
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        PLAYER_CAPES.clear();
    }
}
