package com.platypushasnohat.sinew.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SinewConfig {

    public static IConfigSpec COMMON_CONFIG;

    // common
    public static ModConfigSpec.BooleanValue ENABLE_PROGRESSION;
    public static ModConfigSpec.BooleanValue FIX_LAND_RANDOM_POS;
    public static ModConfigSpec.BooleanValue RESISTANCE_USES_DAMAGE_REDUCTION;
    public static ModConfigSpec.IntValue INSANITY_DAYS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        ENABLE_PROGRESSION = builder.comment("Whether progression tags should be used for mob spawning").define("enableProgression", true);
        FIX_LAND_RANDOM_POS = builder.comment("Whether a fix should be applied to stop mob pathfinding from being biased towards higher locations").define("fixLandRandomPos", true);
        RESISTANCE_USES_DAMAGE_REDUCTION = builder.comment("Whether the Resistance effect should grant 20% Damage Reduction per level instead of using its vanilla damage calculation").gameRestart().define("resistanceUsesDamageReduction", true);

        INSANITY_DAYS = builder.comment("How many in-game days a player has to go without sleeping before insanity lets them perceive spectral mobs tagged #sinew:triggered_by_insanity. 0 disables insanity").defineInRange("insanityDays", 21, 0, 1000);

        COMMON_CONFIG = builder.build();
    }
}
