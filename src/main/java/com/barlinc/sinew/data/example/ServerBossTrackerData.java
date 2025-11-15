package com.barlinc.sinew.data.example;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.barlinc.sinew.data.SimpleSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class ServerBossTrackerData extends SimpleSavedData<ServerBossTrackerData> {
    public static final String ID = "boss_tracker_data";

    public static final Codec<ServerBossTrackerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.LONG.optionalFieldOf("firstKillTime", -1L).forGetter(data -> data.firstKillTime),
        Codec.BOOL.optionalFieldOf("defeated", false).forGetter(data -> data.defeated)
    ).apply(instance, ServerBossTrackerData::new));

    public long firstKillTime = -1;
    public boolean defeated = false;

    public ServerBossTrackerData(long firstKillTime, boolean defeated) {
        this.firstKillTime = firstKillTime;
        this.defeated = defeated;
    }

    public ServerBossTrackerData(CompoundTag tag) {
        super(tag);
    }

    /** Empty constructor (used by computeIfAbsent when no file exists yet) */
    public ServerBossTrackerData() {}

    @Override
    protected Codec<ServerBossTrackerData> getCodec() {
        return CODEC;
    }

    @Override
    protected void copyFrom(ServerBossTrackerData other) {
        this.firstKillTime = other.firstKillTime;
        this.defeated = other.defeated;
    }

    public static ServerBossTrackerData get(Level level) {
        return SimpleSavedData.get(level, ServerBossTrackerData::new, ServerBossTrackerData::new, ID);
    }
}
