package com.platypushasnohat.sinew.network;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.supporter.SupporterCape;
import com.platypushasnohat.sinew.supporter.SupporterCapes;
import com.platypushasnohat.sinew.supporter.SupporterCapesClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SupporterCapePacket(UUID player, SupporterCape cape) implements CustomPacketPayload {

    public static final Type<SupporterCapePacket> TYPE = new Type<>(Sinew.location("supporter_cape"));

    public static final StreamCodec<ByteBuf, SupporterCapePacket> CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, SupporterCapePacket::player, SupporterCape.STREAM_CODEC, SupporterCapePacket::cape, SupporterCapePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SupporterCapePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isServerbound()) {
                SupporterCapes.setCape((ServerPlayer) context.player(), packet.cape());
            }
            else {
                SupporterCapesClient.setOtherCape(packet.player(), packet.cape());
            }
        });
    }
}
