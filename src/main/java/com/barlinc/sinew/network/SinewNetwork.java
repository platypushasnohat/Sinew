package com.barlinc.sinew.network;

import com.barlinc.sinew.Sinew;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class SinewNetwork {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Sinew.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.registerMessage(id(), SyncSavedDataS2CPacket.class,
                SyncSavedDataS2CPacket::encode,
                SyncSavedDataS2CPacket::decode,
                SyncSavedDataS2CPacket::handle);

        net.registerMessage(id(), ScreenshakeS2CPacket.class,
                ScreenshakeS2CPacket::encode,
                ScreenshakeS2CPacket::decode,
                ScreenshakeS2CPacket::handle);

        net.messageBuilder(LightningDamagePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LightningDamagePacket::new)
                .encoder(LightningDamagePacket::toBytes)
                .consumerMainThread(LightningDamagePacket::handle)
                .add();

        net.messageBuilder(LightningSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LightningSyncPacket::new)
                .encoder(LightningSyncPacket::toBytes)
                .consumerMainThread(LightningSyncPacket::handle)
                .add();



    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}

