package com.platypushasnohat.sinew.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.world.SinewWorldData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

///sinew progression shows the stage, /sinew progression set <pre_nether|post_nether|post_end> changes it
//only changes sinew's world data flags. entering nether / kill dragon sets them again
@EventBusSubscriber(modid = Sinew.MOD_ID)
public class ProgressionCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        //merges with the other /sinew subcommands
        event.getDispatcher().register(Commands.literal("sinew").requires(source -> source.hasPermission(2)).then(Commands.literal("progression")
                .executes(context -> {
                    SinewWorldData worldData = SinewWorldData.get(context.getSource().getServer().overworld());
                    String stage = worldData.hasDragonBeenKilledBefore() ? "post_end" : worldData.hasNetherBeenEnteredBefore() ? "post_nether" : "pre_nether";
                    context.getSource().sendSuccess(() -> Component.literal("Sinew progression stage: " + stage), false);
                    return 1;
                })
                .then(Commands.literal("set")
                        .then(setStage("pre_nether", false, false))
                        .then(setStage("post_nether", true, false))
                        .then(setStage("post_end", true, true)))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> setStage(String stage, boolean netherEntered, boolean dragonKilled) {
        return Commands.literal(stage).executes(context -> {
            SinewWorldData worldData = SinewWorldData.get(context.getSource().getServer().overworld());
            worldData.setHasNetherBeenEnteredBefore(netherEntered);
            worldData.setHasDragonBeenKilledBefore(dragonKilled);
            context.getSource().sendSuccess(() -> Component.literal("Set Sinew progression stage to " + stage), true);
            return 1;
        });
    }
}
