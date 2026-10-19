package com.platypushasnohat.sinew.entity.spectral;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

//entities that cant see it never receive it on client, cant hurt, push, or target it
public interface SpectralEntity {

    //whether natural spawns need a player who can perceive this mob nearby
    default boolean requiresPerceiverToSpawn() {
        return false;
    }

    default double getPerceiverSpawnRange() {
        return 32.0D;
    }

    //ticks a perceiving player has to keep staring at this mob before it targets them, 100 = 5 seconds
    //used by: every spectral mob that adds SpectralStareGoal. the Sulcus (formerly Grell) own materialize stare goal
    default int getStareTicksToTarget() {
        return 100;
    }

    //lets this mob be perceived by viewers on its own in addition to their sight sources
    //used by: Sulcus (formerly grell) (materializing for the starer + staying seen after it aggros + its fake copies follow the real one + everyone once enraged at half health)
    //used by Parasoul (everyone sees once pacified with sake), Icicat on revealing itself, Wight while not in retreat mode
    //use SpectralSight.refreshTracking(this) when the answer changes so clients get the mob added or removed
    //IMPORTANT SYNCING INFO: if the mob is also visible without sight, client MUST call this too, through SpectralSight.isPerceivedByClient
    //MUST sync whatever it reads (entity data UUID etc) or the client will treat the mob as intangible while the server treats player as being able to interact with it
    default boolean isPerceivedBy(Entity viewer) {
        return false;
    }

    //if the viewer still gets the mob on their client when they can't perceive it
    //used by: Roamer (watching from afar) Ghost (floating held tool) Icicat (eyes while stalking)
    //used by: Envoy (outline for players with its effect) Sovereign (eyes for players with Observed)
    //it stays intangible: no crosshair + no hitbox + no glow + no fire + can't hurt it
    //renderer decides what they see, check SpectralSight.isPerceivedByClient(this)
    //IMPORTANT SYNCING INFO: client calls this with the local player, so it must give the same answer on both sides.
    //viewer's own effects are already synced to their client. anything else needs sync, call SpectralSight.refreshTracking(this) when changes are there
    default boolean isVisibleWithoutSight(Player viewer) {
        return false;
    }

    //if the players who can't perceive this mob still hear its sounds
    //used by: Ghost Haunt (ghostly noises) Parasoul (shockwave sounds)
    default boolean canBeHeardBy(Player listener) {
        return false;
    }

    //damage from anything that can't perceive this mob is ignored, except /kill and the void
    //used by: every spectral mob
    default boolean canBeHurtBy(DamageSource source) {
        return source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || SpectralSight.canPerceive(source.getEntity(), (Entity) this);
    }

    //if target lost sight of this mob and no other perceiving player was close enough to switch to
    //used by: every spectral mob, e.g. taking off goggles while insanity can't see the mob
    default void onTargetLostSight(LivingEntity lostTarget) {
        ((Entity) this).discard();
    }
}
