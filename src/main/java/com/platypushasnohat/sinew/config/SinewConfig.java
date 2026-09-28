package com.platypushasnohat.sinew.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SinewConfig {

    public static IConfigSpec COMMON_CONFIG;

    // common
    public static ModConfigSpec.BooleanValue ENABLE_PROGRESSION;
    public static ModConfigSpec.BooleanValue FIX_LAND_RANDOM_POS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ENABLE_PROGRESSION = builder.comment("Whether progression tags should be used for mob spawning").define("enableProgression", true);
        FIX_LAND_RANDOM_POS = builder.comment("Whether a fix should be applied to stop mob pathfinding from being biased towards higher locations").define("fixLandRandomPos", true);
        COMMON_CONFIG = builder.build();
    }
}
