package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Sinew.MOD_ID)
public class SpectralEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof SpectralEntity spectral && !spectral.canBeHurtBy(event.getSource())) {
            event.setCanceled(true);
        }
        //spectral mobs can't hurt players who can't perceive them, by any means except /kill and the void
        if (event.getEntity() instanceof Player player && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (isUnperceivedSpectral(player, event.getSource().getEntity()) || isUnperceivedSpectral(player, event.getSource().getDirectEntity()))) {
            event.setCanceled(true);
        }
    }

    private static boolean isUnperceivedSpectral(Player player, Entity entity) {
        return entity instanceof SpectralEntity && !SpectralSight.canPerceive(player, entity);
    }

    //projectiles from anything that can't perceive the mob pass through it
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof SpectralEntity && !SpectralSight.canPerceive(event.getProjectile().getOwner(), hitResult.getEntity())) {
            event.setCanceled(true);
        }
        //projectiles from spectral mobs pass through players who can't perceive the mob
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof Player player && isUnperceivedSpectral(player, event.getProjectile().getOwner())) {
            event.setCanceled(true);
        }
    }

    //stops normal mobs from targeting spectral mobs unless they can see them
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget instanceof SpectralEntity && !SpectralSight.canPerceive(event.getEntity(), newTarget)) {
            event.setCanceled(true);
        }
        //and stops spectral mobs from targeting players who can't perceive them
        if (newTarget instanceof Player player && isUnperceivedSpectral(player, event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();
        MobSpawnType spawnType = event.getSpawnType();
        boolean naturalSpawn = spawnType != MobSpawnType.COMMAND && spawnType != MobSpawnType.BUCKET && spawnType != MobSpawnType.SPAWN_EGG && spawnType != MobSpawnType.DISPENSER;
        if (!event.isCanceled() && naturalSpawn && mob instanceof SpectralEntity spectral && spectral.requiresPerceiverToSpawn()) {
            if (!SpectralSight.isPerceiverNearby(event.getLevel(), mob, spectral.getPerceiverSpawnRange())) {
                event.setSpawnCancelled(true);
                event.setCanceled(true);
            }
        }
    }

    //sync spectral sight on dimension change
    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpectralSight.syncSight(player);
        }
    }

    //insanity reads the vanilla time since rest stat phantoms use. sleeping and dying reset
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            int insanityDays = SinewConfig.INSANITY_DAYS.get();
            int timeSinceRest = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            if (insanityDays > 0 && timeSinceRest >= insanityDays * 24000) {
                SpectralSight.addSource(player, SpectralSight.INSANITY);
            } else {
                SpectralSight.removeSource(player, SpectralSight.INSANITY);
            }
        }
    }
}