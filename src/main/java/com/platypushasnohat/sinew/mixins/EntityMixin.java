package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    //server never sends spectral mobs to players who can't perceive them. This makes sure there's nothing on their client to render
    @Inject(method = "broadcastToPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$hideSpectralEntity(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (!SpectralSight.isSentTo(player, entity)) {
            cir.setReturnValue(false);
        }
    }

    //ambient, hurt, death and step sounds only go to perceiving players + canBeHeardBy
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$playSpectralSound(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof SpectralEntity && entity.level() instanceof ServerLevel serverLevel) {
            if (!entity.isSilent()) {
                SpectralSight.playSoundToListeners(serverLevel, entity, sound, entity.getSoundSource(), volume, pitch);
            }
            ci.cancel();
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$noSpectralPushing(Entity other, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().isClientSide()) {
            if (SpectralSight.isIntangibleOnClient(entity) || SpectralSight.isIntangibleOnClient(other)) {
                ci.cancel();
            }
        } else if (!SpectralSight.canPerceive(other, entity) || !SpectralSight.canPerceive(entity, other)) {
            ci.cancel();
        }
    }

    @Inject(method = "ignoreExplosion", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$ignoreUnseenExplosion(Explosion explosion, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof SpectralEntity && !SpectralSight.canPerceive(explosion.getIndirectSourceEntity(), entity)) {
            cir.setReturnValue(true);
        }
    }

    //only matters for mobs that are visible without sight, everything else isn't on the client unless perceived
    //used by: Roamer + Ghost + Icicat + Envoy + Sovereign

    @Inject(method = "isPickable", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$unpickableSpectral(CallbackInfoReturnable<Boolean> cir) {
        if (SpectralSight.isIntangibleOnClient((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isCurrentlyGlowing", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$noSpectralGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (SpectralSight.isIntangibleOnClient((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "displayFireAnimation", at = @At(value = "HEAD"), cancellable = true)
    private void sinew$noSpectralFire(CallbackInfoReturnable<Boolean> cir) {
        if (SpectralSight.isIntangibleOnClient((Entity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }
}
