package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow
    @Final
    private Abilities abilities;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    // todo: this doesn't work for some reason
    @Inject(method = "getFlyingSpeed", at = @At(value = "RETURN"), cancellable = true, remap = false)
    protected void sinew$getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        if (!this.isPassenger() && !this.abilities.flying) {
            double airSpeed = 0.0D;
            if (this.getAttribute(SinewAttributes.AIR_SPEED) != null) {
                airSpeed = this.getAttributeValue(SinewAttributes.AIR_SPEED);
            }
            double airSpeedModifier = airSpeed / 10;
            if (airSpeed > 0.0D) {
                cir.setReturnValue((float) (this.isSprinting() ? 0.025999999F + airSpeedModifier : 0.02F + airSpeedModifier));
            }
        }
    }
}