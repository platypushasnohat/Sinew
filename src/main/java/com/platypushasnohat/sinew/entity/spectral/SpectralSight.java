package com.platypushasnohat.sinew.entity.spectral;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.network.SpectralSightPacket;
import com.platypushasnohat.sinew.registry.SinewAttachments;
import com.platypushasnohat.sinew.tags.SinewEntityTags;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

//data attachment with all sources currently letting an entity perceive spectral
public class SpectralSight {

    //used by: Entropic Envoy and #sinew:triggered_by_insanity
    public static final ResourceLocation INSANITY = Sinew.location("insanity");

    //sources without a filter do not use tags
    private static final Map<ResourceLocation, TagKey<EntityType<?>>> SOURCE_FILTERS = new ConcurrentHashMap<>();

    static {
        registerSourceFilter(INSANITY, SinewEntityTags.TRIGGERED_BY_INSANITY);
    }

    private final Set<ResourceLocation> sources = new HashSet<>();

    public static void registerSourceFilter(ResourceLocation source, TagKey<EntityType<?>> filter) {
        SOURCE_FILTERS.put(source, filter);
    }

    public Set<ResourceLocation> getSources() {
        return Set.copyOf(this.sources);
    }

    //client side only
    public void setSources(Collection<ResourceLocation> sources) {
        this.sources.clear();
        this.sources.addAll(sources);
    }

    public boolean canSee(EntityType<?> type) {
        for (ResourceLocation source : this.sources) {
            TagKey<EntityType<?>> filter = SOURCE_FILTERS.get(source);
            if (filter == null || type.is(filter)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSource(Entity holder, ResourceLocation source) {
        return holder.hasData(SinewAttachments.SPECTRAL_SIGHT.get()) && holder.getData(SinewAttachments.SPECTRAL_SIGHT.get()).sources.contains(source);
    }

    public static void addSource(Entity holder, ResourceLocation source) {
        if (holder.getData(SinewAttachments.SPECTRAL_SIGHT.get()).sources.add(source)) {
            syncAndRefresh(holder);
        }
    }

    //spectral mobs hunting the holder or its tames that can no longer perceive switch to another perceiving entity. if not use onTargetLostSight
    public static void removeSource(Entity holder, ResourceLocation source) {
        if (hasSource(holder, source)) {
            List<Mob> hunters = holder.level().getEntitiesOfClass(Mob.class, holder.getBoundingBox().inflate(128.0D), mob -> mob instanceof SpectralEntity && mob.getTarget() != null && isHolderOrOwnedBy(mob.getTarget(), holder) && canPerceive(mob.getTarget(), mob));
            holder.getData(SinewAttachments.SPECTRAL_SIGHT.get()).sources.remove(source);
            syncAndRefresh(holder);
            for (Mob hunter : hunters) {
                LivingEntity target = hunter.getTarget();
                if (target != null && !canPerceive(target, hunter)) {
                    retargetOrLoseSight(hunter, target);
                }
            }
        }
    }

    //tames share their owner's sight, so they lose it when the owner does
    private static boolean isHolderOrOwnedBy(LivingEntity target, Entity holder) {
        return target == holder || (target instanceof OwnableEntity ownable && ownable.getOwner() == holder);
    }

    public static boolean canPerceive(@Nullable Entity viewer, Entity target) {
        if (!(target instanceof SpectralEntity)) {
            return true;
        }
        if (viewer == null) {
            return false;
        }
        if (canPerceiveDirectly(viewer, target)) {
            return true;
        }
        //tamed and owned mobs share their owner's current sight sources and isPerceivedBy, unless overridden with SpectralSightSharing
        //todo, when construct building is in, make it compatible with spectral mobs as well (construct building is a future endeavor)
        if (viewer instanceof OwnableEntity ownable && !(viewer instanceof SpectralSightSharing sharing && !sharing.sharesOwnerSight())) {
            LivingEntity owner = ownable.getOwner();
            return owner != null && owner != viewer && canPerceiveDirectly(owner, target);
        }
        return false;
    }

    //viewer's own perception only, without its owner's
    private static boolean canPerceiveDirectly(Entity viewer, Entity target) {
        if (viewer == target || viewer instanceof SpectralEntity || viewer.isSpectator() || ((SpectralEntity) target).isPerceivedBy(viewer)) {
            return true;
        }
        return viewer.hasData(SinewAttachments.SPECTRAL_SIGHT.get()) && viewer.getData(SinewAttachments.SPECTRAL_SIGHT.get()).canSee(target.getType());
    }

    //whether  server should send this entity to the player's client
    public static boolean isSentTo(ServerPlayer player, Entity target) {
        return !(target instanceof SpectralEntity spectral) || spectral.isVisibleWithoutSight(player) || canPerceive(player, target);
    }

    //client side: whether local player perceives this entity. use it in renderers of mobs that are visible without sight
    //used by: Roamer Ghost Icicat Envoy and Sovereign renderer
    public static boolean isPerceivedByClient(Entity target) {
        Player player = Sinew.PROXY.getClientSidePlayer();
        return !(target instanceof SpectralEntity spectral) || player == null || !spectral.isVisibleWithoutSight(player) || canPerceive(player, target);
    }

    //client side: true for visible without special equipment mob the local player can't perceive. intangible
    //used by: Roamer, Ghost, Icicat, Envoy and Sovereign, through the isPickable, glow, fire, push and hitbox mixins
    //client side: 0 to 1 fade in progress. client entities are created fresh when they appear, so tickCount counts from then
    public static float getFadeIn(Entity entity) {
        if (entity.level().isClientSide() && entity instanceof SpectralEntity spectral && spectral.getFadeInTicks() > 0) {
            return Math.min((float) entity.tickCount / spectral.getFadeInTicks(), 1.0F);
        }
        return 1.0F;
    }

    public static boolean isIntangibleOnClient(Entity target) {
        return target.level().isClientSide() && !isPerceivedByClient(target);
    }

    //for target goals (new NearestAttackableTargetGoal<>(this, Player.class, true, SpectralSight.perceives(this)))
    public static Predicate<LivingEntity> perceives(Entity spectral) {
        return entity -> canPerceive(entity, spectral);
    }

    public static boolean isPerceiverNearby(EntityGetter level, Entity spectral, double range) {
        return !level.getEntitiesOfClass(Player.class, spectral.getBoundingBox().inflate(range), player -> !player.isSpectator() && player.distanceToSqr(spectral) <= range * range && canPerceive(player, spectral)).isEmpty();
    }

    //true if the player's view line hits the entity's hitbox (padded half a block) or points within 15 degrees of its center
    public static boolean isLookingAt(Player player, Entity entity) {
        return isLookingAt(player, entity, 15.0D);
    }

    public static boolean isLookingAt(Player player, Entity entity, double maxAngle) {
        Vec3 eye = player.getEyePosition();
        Vec3 view = player.getViewVector(1.0F);
        Vec3 toCenter = entity.getBoundingBox().getCenter().subtract(eye);
        Vec3 end = eye.add(view.scale(toCenter.length() + entity.getBbWidth() + entity.getBbHeight()));
        AABB hitbox = entity.getBoundingBox().inflate(0.5D);
        boolean aimed = hitbox.contains(eye) || hitbox.clip(eye, end).isPresent() || view.dot(toCenter.normalize()) >= Math.cos(Math.toRadians(maxAngle));
        return aimed && player.hasLineOfSight(entity);
    }

    public static void retargetOrLoseSight(Mob mob, LivingEntity lostTarget) {
        double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        List<Player> players = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(range), player -> player != lostTarget && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && canPerceive(player, mob));
        Player newTarget = players.stream().min(Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        if (newTarget != null) {
            mob.setTarget(newTarget);
        } else {
            mob.setTarget(null);
            ((SpectralEntity) mob).onTargetLostSight(lostTarget);
        }
    }

    public static <T extends ParticleOptions> void sendParticlesToPerceivers(ServerLevel level, Entity spectral, T particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
        for (ServerPlayer player : level.players()) {
            if (canPerceive(player, spectral)) {
                level.sendParticles(player, particle, false, x, y, z, count, xDist, yDist, zDist, speed);
            }
        }
    }

    //used by: every spectral mob through EntityMixin + Ghost, Haunt and Parasoul reach non-perceivers through canBeHeardBy
    public static void playSoundToListeners(ServerLevel level, Entity spectral, SoundEvent sound, SoundSource source, float volume, float pitch) {
        Holder<SoundEvent> holder = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound);
        double range = sound.getRange(volume);
        long seed = level.getRandom().nextLong();
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(spectral) < range * range && (canPerceive(player, spectral) || ((SpectralEntity) spectral).canBeHeardBy(player))) {
                player.connection.send(new ClientboundSoundPacket(holder, source, spectral.getX(), spectral.getY(), spectral.getZ(), volume, pitch, seed));
            }
        }
    }

    public static void syncSight(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new SpectralSightPacket(player.hasData(SinewAttachments.SPECTRAL_SIGHT.get()) ? player.getData(SinewAttachments.SPECTRAL_SIGHT.get()).getSources() : Set.of()));
    }

    //re-runs entity tracking for the player so spectral mobs are added to or removed from their client immediately
    public static void refreshTracking(ServerPlayer player) {
        player.serverLevel().getChunkSource().move(player);
    }

    //re-runs tracking for every player in the spectral mob's level after isPerceivedBy or isVisibleWithoutSight changed
    //used by: Sulcus (formerly the grell) materializing + fading + enraging, Parasoul pacified state, Icicat coming out of hunt, Wight going into retreat mode, Envoy and Sovereign and any other astral-spectral (effect gained or lost)
    //goes over every tracked entity per player, call on state change. do not call every tick
    public static void refreshTracking(Entity spectral) {
        if (spectral.level() instanceof ServerLevel serverLevel) {
            for (ServerPlayer player : serverLevel.players()) {
                refreshTracking(player);
            }
        }
    }

    private static void syncAndRefresh(Entity holder) {
        if (holder instanceof ServerPlayer player) {
            syncSight(player);
            refreshTracking(player);
        }
    }
}
