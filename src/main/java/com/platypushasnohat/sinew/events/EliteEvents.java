package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.entity.elite.EliteMob;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Collection;

@EventBusSubscriber(modid = Sinew.MOD_ID)
public class EliteEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        MobSpawnType spawnType = event.getSpawnType();
        boolean naturalSpawn = spawnType != MobSpawnType.COMMAND && spawnType != MobSpawnType.BUCKET && spawnType != MobSpawnType.SPAWN_EGG && spawnType != MobSpawnType.DISPENSER;
        if (!event.isCanceled() && naturalSpawn && SinewConfig.ELITE_MOBS.get() && mob instanceof EliteMob elite && mob.getRandom().nextDouble() < elite.getEliteChance()) {
            elite.makeElite();
        }
    }

    //2x loot and custom table if we want elite exclusive drops later on
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof EliteMob elite && elite.isElite() && entity.level() instanceof ServerLevel serverLevel) {
            LootParams params = createLootParams(serverLevel, entity, event.getSource());
            addDrops(serverLevel, entity, entity.getLootTable(), params, event.getDrops());
            ResourceKey<LootTable> eliteLootTable = elite.getEliteLootTable();
            if (eliteLootTable != null) {
                addDrops(serverLevel, entity, eliteLootTable, params, event.getDrops());
            }
        }
    }

    private static LootParams createLootParams(ServerLevel level, LivingEntity entity, DamageSource source) {
        LootParams.Builder builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
        if (source.getEntity() instanceof Player player) {
            builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }
        return builder.create(LootContextParamSets.ENTITY);
    }

    private static void addDrops(ServerLevel level, LivingEntity entity, ResourceKey<LootTable> key, LootParams params, Collection<ItemEntity> drops) {
        LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
        table.getRandomItems(params, stack -> {
            ItemEntity item = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), stack);
            item.setDefaultPickUpDelay();
            drops.add(item);
        });
    }
}
