package com.platypushasnohat.sinew.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SinewConfig {

    public static IConfigSpec COMMON_CONFIG;

    // common
    public static ModConfigSpec.BooleanValue SEND_PROGRESSION_MESSAGE;
    public static ModConfigSpec.BooleanValue ENABLE_PROGRESSION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SEND_PROGRESSION_MESSAGE = builder.comment("Whether entering the nether or defeating the ender dragon should send a message to all players").define("sendProgressionMessage", true);
        ENABLE_PROGRESSION = builder.comment("Whether progression tags should be used for mob spawning").define("enableProgression", true);
        COMMON_CONFIG = builder.build();
    }
}
