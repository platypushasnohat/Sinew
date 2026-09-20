package com.platypushasnohat.sinew.network;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MultipartEntityPacket implements CustomPacketPayload {

    public static final Type<MultipartEntityPacket> TYPE = new Type<>(Sinew.location("multipart_entity"));

    public static final StreamCodec<FriendlyByteBuf, MultipartEntityPacket> CODEC = StreamCodec.ofMember(MultipartEntityPacket::write, MultipartEntityPacket::read);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public int parentId;
    public int playerId;
    public int type;

    public MultipartEntityPacket(int parentId, int playerId, int type) {
        this.parentId = parentId;
        this.playerId = playerId;
        this.type = type;
    }

    public MultipartEntityPacket() {
    }

    public static MultipartEntityPacket read(FriendlyByteBuf buf) {
        return new MultipartEntityPacket(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void write(MultipartEntityPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.parentId);
        buf.writeInt(message.playerId);
        buf.writeInt(message.type);
    }

    public static void handle(MultipartEntityPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!player.level().isClientSide) {
                Entity parent = player.level().getEntity(message.parentId);
                if (parent != null && parent.isMultipartEntity() && player.distanceTo(parent) < 16) {
                    if (message.type == 0) {
                        parent.interact(player, player.getUsedItemHand());
                    } else if (message.type == 1) {
                        player.attack(parent);
                    }
                }
            }
        });
    }
}