package com.barlinc.sinew.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A simplified {@link SavedData} base class that uses a {@link Codec} for automatic serialization and deserialization.
 * This class is intended for server-side only. Attempting to use it on the client will throw an exception.
 *
 * @param <T> Your concrete data type extending this class.
 */
public abstract class SimpleSavedData<T extends SimpleSavedData<T>> extends SavedData {
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Default constructor for creating new, empty instances.
     */
    protected SimpleSavedData() {}

    /**
     * Constructor for loading from NBT.
     *
     * @param tag The saved tag to load from.
     */
    protected SimpleSavedData(CompoundTag tag) {
        getCodec().parse(NbtOps.INSTANCE, tag.get("data"))
                .resultOrPartial(msg -> LOGGER.error("[SavedData] Error parsing data: {}", msg))
                .ifPresent(decoded -> copyFrom((T) decoded));
    }

    /**
     * @return The codec used to serialize/deserialize this saved data.
     */
    protected abstract Codec<T> getCodec();

    /**
     * Copies the data from another instance of this saved data.
     * Called internally during loading and syncing.
     *
     * @param other The instance to copy data from.
     */
    protected abstract void copyFrom(T other);

    @Override
    public CompoundTag save(CompoundTag tag) {
        getCodec().encodeStart(NbtOps.INSTANCE, (T) this)
                .resultOrPartial(msg -> LOGGER.error("[SavedData] Error saving data: {}", msg))
                .ifPresent(result -> tag.put("data", result));
        return tag;
    }

    /**
     * Retrieves or creates the server-side instance of this SavedData.
     *
     * @param level      The level to retrieve the data from (must be server-side).
     * @param loadFunc   A constructor or method to load from NBT (e.g. {@code MyData::new}).
     * @param createFunc A constructor or method to create a new empty instance (e.g. {@code MyData::new}).
     * @param id         The unique ID for this data.
     * @param <T>        Your data type.
     * @return The instance of your SavedData.
     */
    public static <T extends SimpleSavedData<T>> T get(Level level, Function<CompoundTag, T> loadFunc, Supplier<T> createFunc, String id) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Attempted to access server-only SavedData on the client: " + id);
        }

        DimensionDataStorage storage = serverLevel.getDataStorage();
        return storage.computeIfAbsent(loadFunc, createFunc, id);
    }
}




