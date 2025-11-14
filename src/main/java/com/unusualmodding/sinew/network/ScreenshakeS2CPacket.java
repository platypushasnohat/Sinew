package com.unusualmodding.sinew.network;

import com.unusualmodding.sinew.managers.ScreenshakeManager;
import com.unusualmodding.sinew.system.screenshake.ScreenshakeBuilder;
import com.unusualmodding.sinew.system.screenshake.ScreenshakeInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScreenshakeS2CPacket {
    public final ScreenshakeInstance instance;

    public ScreenshakeS2CPacket(Consumer<ScreenshakeBuilder> constructor) {
        ScreenshakeBuilder builder = ScreenshakeBuilder.create();
        constructor.accept(builder);
        this.instance = builder.build();
    }

    public ScreenshakeS2CPacket(ScreenshakeInstance instance) {
        this.instance = instance;
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag encodedTag = (CompoundTag) (ScreenshakeInstance.CODEC.encodeStart(NbtOps.INSTANCE, instance).result().orElse(new CompoundTag()));
        buf.writeNbt(encodedTag);
    }
    public static ScreenshakeS2CPacket decode(FriendlyByteBuf buffer) {
        CompoundTag receivedTag = buffer.readNbt();
        ScreenshakeInstance decodedMap = ScreenshakeInstance.CODEC.parse(NbtOps.INSTANCE, receivedTag).result().orElse(ScreenshakeBuilder.create().build());
        return new ScreenshakeS2CPacket(decodedMap);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ScreenshakeManager.addScreenshake(instance);
        });
        return true;
    }

}
