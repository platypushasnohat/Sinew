package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import com.platypushasnohat.sinew.registry.SinewAttachments;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

//DEBUG
///sinew spectral add <id>, /sinew spectral remove <id>, /sinew spectral list. any id that isn't sinew:insanity acts like goggles (sees everything)
//sinew:insanity gets removed again within a second unless you're actually insane
@EventBusSubscriber(modid = Sinew.MOD_ID)
public class SpectralDebugCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        //merges with the other /sinew subcommands
        event.getDispatcher().register(Commands.literal("sinew").requires(source -> source.hasPermission(2)).then(Commands.literal("spectral")
                .then(Commands.literal("add").then(Commands.argument("source", ResourceLocationArgument.id()).executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ResourceLocation source = ResourceLocationArgument.getId(context, "source");
                    SpectralSight.addSource(player, source);
                    context.getSource().sendSuccess(() -> Component.literal("Added spectral sight source " + source), false);
                    return 1;
                })))
                .then(Commands.literal("remove").then(Commands.argument("source", ResourceLocationArgument.id()).executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ResourceLocation source = ResourceLocationArgument.getId(context, "source");
                    SpectralSight.removeSource(player, source);
                    context.getSource().sendSuccess(() -> Component.literal("Removed spectral sight source " + source), false);
                    return 1;
                })))
                .then(Commands.literal("list").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    String sources = player.getData(SinewAttachments.SPECTRAL_SIGHT.get()).getSources().toString();
                    context.getSource().sendSuccess(() -> Component.literal("Spectral sight sources: " + sources), false);
                    return 1;
                }))));
    }
}
