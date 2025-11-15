package com.barlinc.sinew.data;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.barlinc.sinew.network.SinewNetwork;
import com.barlinc.sinew.network.SyncSavedDataS2CPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A base {@link SavedData} class that supports both server-side persistence and client-side access.
 * <p>
 * On the server, it behaves like standard {@code SavedData}, storing data to disk using Minecraft's
 * {@link DimensionDataStorage}. On the client, it maintains a temporary in-memory cache and supports
 * data synchronization via network packets using a {@link Codec}.
 * </p>
 *
 * <p><b>Important:</b> Data is not automatically synced from server to client. You must explicitly
 * implement a packet system that calls {@link #applySync(ResourceLocation, CompoundTag)} on the client.</p>
 *
 * @param <T> Your subclass type (e.g. {@code MyWorldData extends ClientAccessibleSavedData<MyWorldData>})
 */

public abstract class ClientAccessibleSavedData<T extends ClientAccessibleSavedData<T>> extends SavedData {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<ResourceLocation, ClientAccessibleSavedData<?>> clientCache = new HashMap<>();
    private static ClientLevel cachedClientLevel = null;
    /** The ID used for saving/syncing (set automatically in get()) */
    protected  ResourceLocation id;

    /** The level this instance is associated with */
    protected  Level level;


    /**
     * Default constructor for creating a new, blank instance (server-side or client-side cache).
     */
    protected ClientAccessibleSavedData() {}
    /**
     * Constructor used for loading from NBT (called by Minecraft's data system).
     *
     * @param tag The compound tag containing serialized data.
     */
    protected ClientAccessibleSavedData(CompoundTag tag) {
        getCodec().parse(NbtOps.INSTANCE, tag.get("data"))
                .resultOrPartial(msg -> LOGGER.error("[SavedData] Error parsing data: {}", msg))
                .ifPresent(decoded -> copyFrom((T) decoded));
    }
    /**
     * Must return the {@link Codec} used to serialize and deserialize this data class.
     *
     * @return The codec for this SavedData implementation.
     */
    protected abstract Codec<T> getCodec();

    /**
     * Copies all data fields from another instance of this class.
     * This is used when decoding from a {@link Codec} or syncing data.
     *
     * @param other The instance to copy from.
     */
    protected abstract void copyFrom(T other);



    public ResourceLocation getId() {
        return this.id;
    }


    @Override
    public void setDirty() {
        super.setDirty();
        if (level instanceof ServerLevel && id != null) {
            CompoundTag tag = new CompoundTag();
            this.save(tag);
            SinewNetwork.sendToClients(new SyncSavedDataS2CPacket(id, tag));
        }
    }


    /**
     * Saves this data to NBT using the provided {@link Codec}.
     *
     * @param tag The compound tag to write into.
     * @return The same tag, with serialized data written under the "data" key.
     */
    @Override
    public CompoundTag save(CompoundTag tag) {
        getCodec().encodeStart(NbtOps.INSTANCE, (T) this)
                .resultOrPartial(msg -> LOGGER.error("[SavedData] Error saving data: {}", msg))
                .ifPresent(result -> tag.put("data", result));
        return tag;
    }

    /**
     * Retrieves a {@link ClientAccessibleSavedData} instance for the given level and ID.
     * <p>
     * This handles both server-side persistent storage and client-side temporary caching.
     * </p>
     *
     * @param level      The current world/dimension.
     * @param loadFunc   A constructor or method to load from NBT.
     * @param createFunc A constructor or method to create a new blank instance.
     * @param id         The unique {@link ResourceLocation} for this data.
     * @param <T>        Your subclass type.
     * @return The corresponding data instance.
     */
    public static <T extends ClientAccessibleSavedData<T>> T get(Level level, Function<CompoundTag, T> loadFunc, Supplier<T> createFunc, ResourceLocation id) {
        T data;

        if (level instanceof ServerLevel serverLevel) {
            DimensionDataStorage storage = serverLevel.getDataStorage();
            data = storage.computeIfAbsent(loadFunc, createFunc, id.toString());
        } else {
            if (cachedClientLevel != level) {
                cachedClientLevel = (ClientLevel) level;
                clientCache.clear();
            }
            data = (T) clientCache.computeIfAbsent(id, k -> createFunc.get());
        }

        data.id = id;
        data.level = level;
        return data;
    }

    /**
     * Applies a synchronization payload sent from the server by deserializing the tag using the appropriate {@link Codec}
     * and applying its data to the current cached client instance.
     *
     * @param id The identifier of the data (must match the registered ID used in {@link #get}).
     * @param tag The serialized sync data.
     */
    @ApiStatus.Internal
    public static void applySync(ResourceLocation id, CompoundTag tag) {
        if (cachedClientLevel != null) {
            ClientAccessibleSavedData<?> data = clientCache.get(id);
            if (data != null) {
                data.getCodec().parse(NbtOps.INSTANCE, tag.get("data"))
                        .resultOrPartial(msg -> LOGGER.error("Error syncing {}: {}", id, msg))
                        .ifPresent(decoded -> ((ClientAccessibleSavedData) data).copyFrom(decoded));
            }
        }
    }
}


