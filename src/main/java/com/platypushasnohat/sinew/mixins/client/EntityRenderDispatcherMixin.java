package com.platypushasnohat.sinew.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    //no F3 + B hitbox for visible without sight spectral mobs
    //used by: Roamer + Ghost + Icicat + Envoy + Sovereign
    @Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true, require = 0)
    private static void sinew$hideSpectralHitbox(CallbackInfo ci, @Local(argsOnly = true) Entity entity) {
        if (SpectralSight.isIntangibleOnClient(entity)) {
            ci.cancel();
        }
    }
}
