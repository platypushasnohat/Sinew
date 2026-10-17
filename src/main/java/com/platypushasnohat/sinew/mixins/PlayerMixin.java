package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.registry.SinewAttributes;
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
}