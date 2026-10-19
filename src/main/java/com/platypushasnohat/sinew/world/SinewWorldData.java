package com.platypushasnohat.sinew.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SinewWorldData extends SavedData {

    private static final String IDENTIFIER = "sinew_world_data";
    public static final Factory<SinewWorldData> FACTORY = new Factory<>(SinewWorldData::new, SinewWorldData::load);

    private boolean hasNetherBeenEnteredBefore = false;
    private boolean hasDragonBeenKilledBefore = false;
    private boolean dragonFlagInitialized = false; //on server start create so old worlds dont get bricked

    private SinewWorldData() {
        super();
    }

    public static SinewWorldData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            DimensionDataStorage storage = overworld.getDataStorage();
            SinewWorldData worldData = storage.computeIfAbsent(FACTORY, IDENTIFIER);
            worldData.setDirty();
            return worldData;
        }
        return null;
    }

    public static SinewWorldData load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        SinewWorldData worldData = new SinewWorldData();
        worldData.hasNetherBeenEnteredBefore = compoundTag.getBoolean("NetherEntered");
        worldData.hasDragonBeenKilledBefore = compoundTag.getBoolean("DragonKilled");
        worldData.dragonFlagInitialized = compoundTag.contains("DragonKilled");
        return worldData;
    }

    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putBoolean("NetherEntered", this.hasNetherBeenEnteredBefore);
        compound.putBoolean("DragonKilled", this.hasDragonBeenKilledBefore);
        return compound;
    }

    public boolean hasNetherBeenEnteredBefore(){
        return this.hasNetherBeenEnteredBefore;
    }

    public void setHasNetherBeenEnteredBefore(boolean hasNetherBeenEnteredBefore) {
        this.hasNetherBeenEnteredBefore = hasNetherBeenEnteredBefore;
    }

    public boolean hasDragonBeenKilledBefore() {
        return this.hasDragonBeenKilledBefore;
    }

    public void setHasDragonBeenKilledBefore(boolean hasDragonBeenKilledBefore) {
        this.hasDragonBeenKilledBefore = hasDragonBeenKilledBefore;
        this.dragonFlagInitialized = true;
    }

    public boolean isDragonFlagInitialized() {
        return this.dragonFlagInitialized;
    }
}