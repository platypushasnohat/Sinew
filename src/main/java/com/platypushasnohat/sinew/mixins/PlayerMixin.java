package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.item.CustomSweepParticle;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @SuppressWarnings("SuspiciousNameCombination")
    @Inject(method = "sweepAttack", at = @At(value = "HEAD"), cancellable = true)
    public void sinew$sweepAttack(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player.getMainHandItem().getItem() instanceof CustomSweepParticle sweepParticle) {
            double ySin = -Mth.sin(player.getYRot() * Mth.DEG_TO_RAD);
            double yCos = Mth.cos(player.getYRot() * Mth.DEG_TO_RAD);
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(sweepParticle.getSweepParticle(), player.getX() + ySin, player.getY(0.5F), player.getZ() + yCos, 0, ySin, 0.0F, yCos, 0.0F);
            }
            ci.cancel();
        }
    }
}