package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.entity.base.TamableMonster;
import com.platypushasnohat.sinew.entity.utils.SplitMagicBonus;
import com.platypushasnohat.sinew.entity.utils.SplitProjectileBonus;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import com.platypushasnohat.sinew.registry.SinewDamageTypes;
import com.platypushasnohat.sinew.tags.SinewDamageTypeTags;
import com.platypushasnohat.sinew.tags.SinewEntityTags;
import com.platypushasnohat.sinew.utils.SinewSoundUtils;
import com.platypushasnohat.sinew.world.SinewWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.List;

@EventBusSubscriber(modid = Sinew.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityModifyAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            event.add(entityType, SinewAttributes.PROJECTILE_DAMAGE);
            event.add(entityType, SinewAttributes.AIR_SPEED);
            event.add(entityType, SinewAttributes.EXPERIENCE_BOOST);
            event.add(entityType, SinewAttributes.MAGIC_DAMAGE);
            event.add(entityType, SinewAttributes.DODGE_CHANCE);
            event.add(entityType, SinewAttributes.LIFE_STEAL);
            event.add(entityType, SinewAttributes.ARMOR_PIERCING);
            event.add(entityType, SinewAttributes.DAMAGE_REDUCTION);
            event.add(entityType, SinewAttributes.FISHING_LUCK);
            event.add(entityType, SinewAttributes.FISHING_LURE);
            event.add(entityType, SinewAttributes.FORTUNE);
            event.add(entityType, SinewAttributes.LOOTING);
            event.add(entityType, SinewAttributes.AMMO_SAVE_CHANCE);
            event.add(entityType, SinewAttributes.MANA_CONSERVE_CHANCE);
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

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();

        // dodge
        if (!(event.getSource().is(SinewDamageTypeTags.BYPASSES_DODGE) || target.getAttribute(SinewAttributes.DODGE_CHANCE) == null)) {
            double dodgeChance = target.getAttributeValue(SinewAttributes.DODGE_CHANCE);
            if (dodgeChance > 0.0D && target.getRandom().nextDouble() < dodgeChance) {
                target.level().playSound(null, target.blockPosition(), SoundEvents.EGG_THROW, target.getSoundSource(), 0.3F, SinewSoundUtils.randomizePitch(target.level()));
                event.setCanceled(true);
            }
        }

        // armor pierce
        if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && source.getEntity() instanceof LivingEntity attacker && attacker.getAttribute(SinewAttributes.ARMOR_PIERCING) != null) {
            float armorPiercing = (float) Mth.clamp(attacker.getAttributeValue(SinewAttributes.ARMOR_PIERCING), 0.0D, 1.0D);
            if (armorPiercing > 0.0F) {
                event.getContainer().addModifier(DamageContainer.Reduction.ARMOR, (container, reduction) -> {
                    float damage = container.getNewDamage();
                    float armor = target.getArmorValue() * (1.0F - armorPiercing);
                    float toughness = (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * (1.0F - armorPiercing);
                    return Math.min(reduction, damage - CombatRules.getDamageAfterAbsorb(target, damage, source, armor, toughness));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        if (!source.is(DamageTypeTags.BYPASSES_EFFECTS) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE) && target.getAttribute(SinewAttributes.DAMAGE_REDUCTION) != null) {
            float damageReduction = (float) Mth.clamp(target.getAttributeValue(SinewAttributes.DAMAGE_REDUCTION), 0.0D, 1.0D);
            if (damageReduction > 0.0F) {
                event.setNewDamage(event.getNewDamage() * (1.0F - damageReduction));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        DamageSource source = event.getSource();

        // life steal
        if (source.getEntity() instanceof LivingEntity attacker
                && attacker != target
                && !source.is(SinewDamageTypeTags.DOES_NOT_LIFESTEAL)
                && !target.getType().is(SinewEntityTags.BLOCK_LIFESTEAL)
                && attacker.getAttribute(SinewAttributes.LIFE_STEAL) != null) {
            float lifeSteal = (float) attacker.getAttributeValue(SinewAttributes.LIFE_STEAL);
            float damageDealt = event.getNewDamage();
            if (lifeSteal > 0.0F && damageDealt > 0.0F && attacker.isAlive()) {
                attacker.heal(damageDealt * lifeSteal);
            }
        }

        // layered damage types
        if (source.getEntity() instanceof LivingEntity attacker && !source.is(SinewDamageTypeTags.IS_LAYERED)) {
            if (source.is(DamageTypeTags.IS_PROJECTILE) && !source.is(SinewDamageTypeTags.IGNORES_PROJECTILE_BONUS) && attacker.getAttribute(SinewAttributes.PROJECTILE_DAMAGE) != null) {
                float projectileDamage = (float) attacker.getAttributeValue(SinewAttributes.PROJECTILE_DAMAGE);
                if (source.getDirectEntity() instanceof SplitProjectileBonus split) {
                    projectileDamage /= Math.max(split.getProjectileCount(), 1);
                }
                if (projectileDamage > 0.0F) {
                    target.invulnerableTime = 0;
                    target.hurt(SinewDamageTypes.causeProjectileDamage(level.registryAccess(), attacker, source), projectileDamage);
                }
            }
            if (source.is(SinewDamageTypeTags.TRIGGERS_MAGIC_DAMAGE) && attacker.getAttribute(SinewAttributes.MAGIC_DAMAGE) != null) {
                float magicDamage = (float) attacker.getAttributeValue(SinewAttributes.MAGIC_DAMAGE);
                if (attacker instanceof Player player) {
                    if (!source.is(SinewDamageTypeTags.IGNORES_ATTACK_COOLDOWN_SCALING)) {
                        magicDamage *= player.getAttackStrengthScale(0);
                    }
                }
                if (source.getDirectEntity() instanceof SplitMagicBonus split) {
                    magicDamage /= Math.max(split.getMagicCount(), 1);
                }
                if (magicDamage > 0.0F) {
                    target.invulnerableTime = 0;
                    target.hurt(SinewDamageTypes.causeMagicDamage(level.registryAccess(), attacker, source), magicDamage);
                }
            }
        }
    }

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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon dragon && dragon.level() instanceof ServerLevel serverLevel) {
            SinewWorldData worldData = SinewWorldData.get(serverLevel);
            if (!worldData.hasDragonBeenKilledBefore()) {
                Sinew.LOGGER.info("End progression has been enabled");
                worldData.setHasDragonBeenKilledBefore(true);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        SinewWorldData worldData = SinewWorldData.get(event.getServer().overworld());
        if (!worldData.isDragonFlagInitialized()) {
            boolean dragonKilled = event.getServer().getWorldData().endDragonFightData().dragonKilled() || event.getServer().getWorldData().endDragonFightData().previouslyKilled();
            worldData.setHasDragonBeenKilledBefore(dragonKilled);
        }
    }
}
