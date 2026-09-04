package com.platypushasnohat.sinew.network;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.mixins.client.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ActionBarPacket(Component message, int duration) implements CustomPacketPayload {

    public static final Type<ActionBarPacket> TYPE = new Type<>(Sinew.location("action_bar"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ActionBarPacket> CODEC = CustomPacketPayload.codec(ActionBarPacket::write, ActionBarPacket::read);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        ComponentSerialization.STREAM_CODEC.encode(buf, this.message);
        buf.writeVarInt(this.duration);
    }

    public static ActionBarPacket read(RegistryFriendlyByteBuf buf) {
        Component message = ComponentSerialization.STREAM_CODEC.decode(buf);
        int duration = buf.readVarInt();
        return new ActionBarPacket(message, duration);
    }

    public static void handle(ActionBarPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.gui.setOverlayMessage(packet.message, false);
            GuiAccessor accessor = (GuiAccessor) minecraft.gui;
            accessor.setOverlayMessageTime(packet.duration());
        });
    }
}