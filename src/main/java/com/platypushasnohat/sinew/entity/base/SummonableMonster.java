package com.platypushasnohat.sinew.entity.base;

import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SummonableMonster extends TamableMonster {

    private static final EntityDataAccessor<Boolean> SUMMONED = SynchedEntityData.defineId(SummonableMonster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIFE_TICKS = SynchedEntityData.defineId(SummonableMonster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_LIFE_TICKS = SynchedEntityData.defineId(SummonableMonster.class, EntityDataSerializers.INT);

    private static final Map<UUID, Set<SummonableMonster>> ACTIVE_SUMMONS = new ConcurrentHashMap<>();

    private float summonDamage;
    @Nullable
    private UUID trackedOwner;

    protected SummonableMonster(EntityType<? extends SummonableMonster> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    public static <T extends SummonableMonster> T summon(EntityType<T> entityType, ServerLevel level, Player owner, ItemStack weapon, InteractionHand hand, Vec3 position) {
        T summon = entityType.create(level);
        T result = null;
        if (summon != null && hasMinionSlots(owner, summon.getSummonCost())) {
            summon.moveTo(position.x, position.y, position.z, owner.getYRot(), 0.0F);
            summon.yHeadRot = owner.getYRot();
            summon.yBodyRot = owner.getYRot();
            EventHooks.finalizeMobSpawn(summon, level, level.getCurrentDifficultyAt(summon.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            summon.setSummoned(owner, getSummonStat(owner, weapon, hand, SinewAttributes.SUMMON_DAMAGE), getSummonStat(owner, weapon, hand, SinewAttributes.SUMMON_DURATION));
            if (level.addFreshEntity(summon)) {
                summon.onSummoned(owner);
                result = summon;
            }
        }
        return result;
    }

    public static double getUsedMinionSlots(UUID owner) {
        Set<SummonableMonster> summons = ACTIVE_SUMMONS.get(owner);
        return summons == null ? 0.0D : summons.stream().filter(SummonableMonster::isAlive).mapToDouble(SummonableMonster::getSummonCost).sum();
    }

    public static double getMaxMinionSlots(Player owner) {
        return owner.getAttributes().hasAttribute(SinewAttributes.MINION_SLOTS) ? owner.getAttributeValue(SinewAttributes.MINION_SLOTS) : 0.0D;
    }

    public static boolean hasMinionSlots(Player owner, double cost) {
        return cost <= 0.0D || getUsedMinionSlots(owner.getUUID()) + cost <= getMaxMinionSlots(owner);
    }

    public static double getSummonStat(Player owner, ItemStack weapon, InteractionHand hand, Holder<Attribute> attribute) {
        double[] value = {owner.getAttributes().hasAttribute(attribute) ? owner.getAttributeValue(attribute) : 0.0D};
        if (hand != InteractionHand.MAIN_HAND) {
            weapon.forEachModifier(EquipmentSlot.MAINHAND, (modifierAttribute, modifier) -> {
                if (modifierAttribute.value() == attribute.value() && modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                    value[0] += modifier.amount();
                }
            });
        }
        return value[0];
    }

    public void setSummoned(Player owner, double damage, double durationSeconds) {
        this.setTame(true, false);
        this.setOwnerUUID(owner.getUUID());
        this.setCommand(COMMAND_FOLLOW);
        this.entityData.set(SUMMONED, true);
        this.summonDamage = (float) damage;
        int lifeTicks = Math.max(1, Mth.floor(durationSeconds * 20.0D));
        this.entityData.set(MAX_LIFE_TICKS, lifeTicks);
        this.setLifeTicks(lifeTicks);
        AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && damage > 0.0D) {
            attackDamage.setBaseValue(damage);
        }
    }

    protected void onSummoned(Player owner) {
    }

    public float getSummonCost() {
        return 1.0F;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
        if (!this.level().isClientSide && this.isSummoned() && this.getOwnerUUID() != null) {
            this.trackedOwner = this.getOwnerUUID();
            ACTIVE_SUMMONS.computeIfAbsent(this.trackedOwner, uuid -> ConcurrentHashMap.newKeySet()).add(this);
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (this.trackedOwner != null) {
            ACTIVE_SUMMONS.computeIfPresent(this.trackedOwner, (uuid, summons) -> {
                summons.remove(this);
                return summons.isEmpty() ? null : summons;
            });
            this.trackedOwner = null;
        }
    }

    public void expire() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY(0.5D), this.getZ(), 10, this.getBbWidth() * 0.5D, this.getBbHeight() * 0.5D, this.getBbWidth() * 0.5D, 0.02D);
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isSummoned() && this.isAlive()) {
            this.setLifeTicks(this.getLifeTicks() - 1);
            if (this.getLifeTicks() <= 0) {
                this.expire();
            }
        }
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 motion = new Vec3(x, y, z).normalize().add(this.random.triangle(0.0D, 0.0172275D * inaccuracy), this.random.triangle(0.0D, 0.0172275D * inaccuracy), this.random.triangle(0.0D, 0.0172275D * inaccuracy)).scale(velocity);
        this.setDeltaMovement(motion);
        this.setYRot((float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG));
        this.setXRot((float) (Mth.atan2(motion.y, motion.horizontalDistance()) * Mth.RAD_TO_DEG));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotation(Entity shooter, float xRot, float yRot, float zRot, float velocity, float inaccuracy) {
        float x = -Mth.sin(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
        float y = -Mth.sin((xRot + zRot) * Mth.DEG_TO_RAD);
        float z = Mth.cos(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
        this.shoot(x, y, z, velocity, inaccuracy);
        Vec3 shooterMotion = shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(shooterMotion.x, shooter.onGround() ? 0.0D : shooterMotion.y, shooterMotion.z));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SUMMONED, false);
        builder.define(LIFE_TICKS, -1);
        builder.define(MAX_LIFE_TICKS, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Summoned", this.isSummoned());
        compoundTag.putInt("LifeTicks", this.getLifeTicks());
        compoundTag.putInt("MaxLifeTicks", this.getMaxLifeTicks());
        compoundTag.putFloat("SummonDamage", this.summonDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.entityData.set(SUMMONED, compoundTag.getBoolean("Summoned"));
        this.setLifeTicks(compoundTag.getInt("LifeTicks"));
        this.entityData.set(MAX_LIFE_TICKS, compoundTag.getInt("MaxLifeTicks"));
        this.summonDamage = compoundTag.getFloat("SummonDamage");
    }

    public boolean isSummoned() {
        return this.entityData.get(SUMMONED);
    }

    public int getLifeTicks() {
        return this.entityData.get(LIFE_TICKS);
    }

    public void setLifeTicks(int lifeTicks) {
        this.entityData.set(LIFE_TICKS, lifeTicks);
    }

    public int getMaxLifeTicks() {
        return this.entityData.get(MAX_LIFE_TICKS);
    }

    public float getSummonDamage() {
        return this.summonDamage;
    }

    public boolean canOwnerCommand(Player owner) {
        return !this.isSummoned();
    }

    @Override
    public boolean shouldDropExperience() {
        return !this.isSummoned() && super.shouldDropExperience();
    }

    @Override
    protected boolean shouldDropLoot() {
        return !this.isSummoned() && super.shouldDropLoot();
    }
}
