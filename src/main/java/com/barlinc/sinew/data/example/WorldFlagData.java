package com.barlinc.sinew.data.example;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.data.ClientAccessibleSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * A simple {@link ClientAccessibleSavedData} example that stores a single boolean flag,
 * demonstrating how to use the Sinew SavedData system with client-server syncing and Codecs.
 * <p>
 * The flag is persisted on the server and can be synced to the client with a custom sync packet.
 * This is ideal for tracking things like world events, boss fights, or toggles visible to the client.
 * </p>
 */
public class WorldFlagData extends ClientAccessibleSavedData<WorldFlagData> {
    /** Unique identifier for this SavedData. Used in get(...) and sync packets. */
    public static final ResourceLocation ID = new ResourceLocation("yourmodid", "world_flag_data");

    /**
     * The Codec used to serialize and deserialize this class.
     * This controls how data is written to NBT or synced to the client.
     */
    public static final Codec<WorldFlagData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("flag", false).forGetter(data -> data.flag)
    ).apply(instance, WorldFlagData::new));

    /** The boolean flag being stored. This is synced and saved. */
    public boolean flag = false;

    /**
     * Empty constructor for creating a new instance (server or client cache).
     */
    public WorldFlagData() {}

    /**
     * Codec constructor — used by the Codec to deserialize.
     *
     * @param b The flag value.
     */
    public WorldFlagData(boolean b) {
        this.flag = b;
    }

    /**
     * NBT constructor — used internally by {@link net.minecraft.world.level.saveddata.SavedData} loading.
     *
     * @param tag The saved NBT tag.
     */
    public WorldFlagData(CompoundTag tag) {
        super(tag);
    }

    @Override
    protected Codec<WorldFlagData> getCodec() {
        return CODEC;
    }

    @Override
    protected void copyFrom(WorldFlagData other) {
        this.flag = other.flag;
    }

    /**
     * Retrieves or creates the current instance of this SavedData for the given level.
     * <p>
     * On the server, this will load from disk or create a new instance.
     * On the client, this will retrieve a temporary in-memory version that can be updated via sync.
     * </p>
     *
     * @param level The current world or dimension.
     * @return The instance of this SavedData for that level.
     */
    public static WorldFlagData get(Level level) {
        return ClientAccessibleSavedData.get(level, WorldFlagData::new, WorldFlagData::new, ID);
    }
}
