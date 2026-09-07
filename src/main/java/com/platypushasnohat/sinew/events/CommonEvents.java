package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.registry.SinewSoundEvents;
import com.platypushasnohat.sinew.tags.SinewEntityTags;
import com.platypushasnohat.sinew.world.SinewWorldData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Sinew.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        LevelAccessor level = event.getLevel();
        boolean validSpawn = event.getSpawnType() != MobSpawnType.COMMAND && event.getSpawnType() != MobSpawnType.BUCKET && event.getSpawnType() != MobSpawnType.SPAWN_EGG && event.getSpawnType() != MobSpawnType.DISPENSER;
        if (SinewConfig.ENABLE_PROGRESSION.get()) {
            if (!event.isCanceled()) {
                if (validSpawn && level instanceof ServerLevel serverLevel) {
                    boolean netherEntered = SinewWorldData.get(serverLevel).hasNetherBeenEnteredBefore();
                    boolean postDragon = serverLevel.getServer().getWorldData().endDragonFightData().dragonKilled() || serverLevel.getServer().getWorldData().endDragonFightData().previouslyKilled();
                    if (entity.getType().is(SinewEntityTags.POST_NETHER_SPAWNS) && !netherEntered) {
                        event.setSpawnCancelled(true);
                        event.setCanceled(true);
                    }
                    if (entity.getType().is(SinewEntityTags.POST_END_SPAWNS) && !postDragon) {
                        event.setSpawnCancelled(true);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player entity = event.getEntity();
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
            ServerLevel overworld = serverPlayer.getServer().overworld();
            SinewWorldData worldData = SinewWorldData.get(overworld);
            if (event.getTo() != Level.NETHER) {
                return;
            }
            if (!worldData.hasNetherBeenEnteredBefore()) {
                worldData.setHasNetherBeenEnteredBefore(true);
                if (SinewConfig.SEND_PROGRESSION_MESSAGE.get()) {
                    Sinew.LOGGER.info("Nether progression has been enabled");
                    for (ServerPlayer player : serverPlayer.getServer().getPlayerList().getPlayers()) {
                        player.playNotifySound(SinewSoundEvents.ENTER_NETHER.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                        MutableComponent message = Component.translatable("sinew.nether_progression.enabled").withStyle(ChatFormatting.RED);
                        player.sendSystemMessage(message);
                    }
                }
            }
        }
    }
}
