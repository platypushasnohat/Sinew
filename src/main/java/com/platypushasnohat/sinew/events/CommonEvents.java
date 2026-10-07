package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.entity.base.TamableMonster;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import com.platypushasnohat.sinew.tags.SinewEntityTags;
import com.platypushasnohat.sinew.world.SinewWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.List;

@EventBusSubscriber(modid = Sinew.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityModifyAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            event.add(entityType, SinewAttributes.RANGED_DAMAGE);
            event.add(entityType, SinewAttributes.AIR_SPEED);
            event.add(entityType, SinewAttributes.EXPERIENCE_BOOST);
        });
    }

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
                Sinew.LOGGER.info("Nether progression has been enabled");
                worldData.setHasNetherBeenEnteredBefore(true);
            }
        }
    }

//    @SubscribeEvent
//    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
//        DamageSource source = event.getSource();
//        if (source.getEntity() instanceof LivingEntity attacker) {
//            if (source.is(DamageTypeTags.IS_PROJECTILE) && attacker.getAttribute(SinewAttributes.RANGED_DAMAGE) != null) {
//                float rangedDamage = (float) attacker.getAttributeValue(SinewAttributes.RANGED_DAMAGE);
//                event.setNewDamage(event.getOriginalDamage() + rangedDamage);
//                if (event.getNewDamage() < 0.0F) {
//                    event.setNewDamage(0.0F);
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        double experienceBoost = event.getAmount() * player.getAttributeValue(SinewAttributes.EXPERIENCE_BOOST);
        int base = Mth.floor(experienceBoost);
        double bonus = Mth.frac(experienceBoost);
        if (bonus != 0.0F && Math.random() < bonus) {
            base++;
        }
        event.setAmount(event.getAmount() + base);
    }

    @SubscribeEvent
    public static void onCanPlayerSleep(CanPlayerSleepEvent event) {
        ServerPlayer player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!level.isDay() && !player.isCreative()) {
            Vec3 center = Vec3.atBottomCenterOf(pos);
            List<TamableMonster> list = level.getEntitiesOfClass(TamableMonster.class, new AABB(center.x - 8.0D, center.y - 5.0D, center.z - 8.0D, center.x + 8.0D, center.y + 5.0D, center.z + 8.0D), monster -> monster.isPreventingPlayerRest(player));
            if (!list.isEmpty()) {
                event.setProblem(Player.BedSleepingProblem.NOT_SAFE);
            }
        }
    }
}
