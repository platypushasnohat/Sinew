package com.platypushasnohat.sinew.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getFlyingSpeed", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void sinew$getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        double airSpeed = 0.0D;
        if (player.getAttribute(SinewAttributes.AIR_SPEED) != null) {
            airSpeed = player.getAttributeValue(SinewAttributes.AIR_SPEED);
        }
        float airSpeedModifier = (float) (airSpeed / 10);
        if (airSpeed > 0.0D) {
            cir.setReturnValue(cir.getReturnValue() + airSpeedModifier);
        }
    }

    //damage indicator hearts from hitting a spectral mob only go to players who can perceive it
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), require = 0)
    private int sinew$hideSpectralDamageParticles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed, Operation<Integer> original, @Local(argsOnly = true) Entity target) {
        if (target instanceof SpectralEntity) {
            SpectralSight.sendParticlesToPerceivers(level, target, particle, x, y, z, count, xDist, yDist, zDist, speed);
            return count;
        }
        return original.call(level, particle, x, y, z, count, xDist, yDist, zDist, speed);
    }
}