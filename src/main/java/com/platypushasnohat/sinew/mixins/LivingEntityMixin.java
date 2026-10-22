package com.platypushasnohat.sinew.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.platypushasnohat.sinew.config.SinewConfig;
import com.platypushasnohat.sinew.tags.SinewDamageTypeTags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true, remap = false)
    protected void sinew$cancelLayeredHurtSound(DamageSource source, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.getLastDamageSource() != null && livingEntity.getLastDamageSource().is(SinewDamageTypeTags.IS_LAYERED)) {
            ci.cancel();
        }
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDeathSound()Lnet/minecraft/sounds/SoundEvent;"))
    private SoundEvent sinew$cancelLayeredDeathSound(LivingEntity instance) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.getLastDamageSource() != null && livingEntity.getLastDamageSource().is(SinewDamageTypeTags.IS_LAYERED)) {
            return SoundEvents.EMPTY;
        }
        return this.getDeathSound();
    }

    @ModifyExpressionValue(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private boolean sinew$getDamageAfterMagicAbsorb(boolean hasEffect) {
        return hasEffect && !SinewConfig.RESISTANCE_USES_DAMAGE_REDUCTION.get();
    }
}
