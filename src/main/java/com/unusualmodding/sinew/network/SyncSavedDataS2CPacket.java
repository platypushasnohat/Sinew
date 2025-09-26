package com.unusualmodding.sinew.network;

import com.unusualmodding.sinew.data.ClientAccessibleSavedData;
import com.unusualmodding.sinew.utils.ClientUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet for syncing {@link ClientAccessibleSavedData} from the server to the client.
 * <p>
 * This packet transmits a {@link ResourceLocation} ID and a {@link CompoundTag} representing the saved data's contents.
 * </p>
 */
public class SyncSavedDataS2CPacket {
    private final ResourceLocation id;
    private final CompoundTag data;

    /**
     * Constructs a new packet with the given data.
     *
     * @param id   The unique ID of the SavedData being synced.
     * @param data The NBT data to apply on the client.
     */
    public SyncSavedDataS2CPacket(ResourceLocation id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    /**
     * Writes the packet contents to the network buffer.
     *
     * @param buf The buffer to write to.
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(id);
        buf.writeNbt(data);
    }

    /**
     * Constructs a packet by reading from a network buffer.
     *
     * @param buf The buffer to read from.
     */
    public static SyncSavedDataS2CPacket decode(FriendlyByteBuf buf) {
        ResourceLocation resourceLocation = buf.readResourceLocation();
        CompoundTag tag = buf.readNbt();
        return new SyncSavedDataS2CPacket(resourceLocation, tag);
    }


    /**
     * Handles the packet on the client by applying the sync to the appropriate {@link ClientAccessibleSavedData} instance.
     *
     * @param supplier The network context supplier.
     * @return {@code true} if handled successfully.
     */
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientLevel level = ClientUtils.getLevel();
            if (level != null) {
                ClientAccessibleSavedData.applySync(id, data);
            }
        });
        return true;
    }
}


