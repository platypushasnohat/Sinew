package com.platypushasnohat.sinew.network;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.registry.SinewAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Set;

// tells client which spectral sight sources player has so visible without sight mobs can be intangible
//used by: Roamer Ghost Icicat Envoy Sovereign
public record SpectralSightPacket(Set<ResourceLocation> sources) implements CustomPacketPayload {

    public static final Type<SpectralSightPacket> TYPE = new Type<>(Sinew.location("spectral_sight"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpectralSightPacket> CODEC = CustomPacketPayload.codec(SpectralSightPacket::write, SpectralSightPacket::read);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeCollection(this.sources, FriendlyByteBuf::writeResourceLocation);
    }

    public static SpectralSightPacket read(RegistryFriendlyByteBuf buf) {
        List<ResourceLocation> sources = buf.readList(FriendlyByteBuf::readResourceLocation);
        return new SpectralSightPacket(Set.copyOf(sources));
    }

    public static void handle(SpectralSightPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> context.player().getData(SinewAttachments.SPECTRAL_SIGHT.get()).setSources(packet.sources()));
    }
}
