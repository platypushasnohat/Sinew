package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getFishingTimeReduction", at = @At(value = "RETURN"), cancellable = true)
    private static void sinew$getFishingTimeReduction(ServerLevel level, ItemStack stack, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof LivingEntity living && living.getAttribute(SinewAttributes.FISHING_LURE) != null) {
            cir.setReturnValue(cir.getReturnValue() + (float) living.getAttributeValue(SinewAttributes.FISHING_LURE));
        }
    }

    @Inject(method = "getFishingLuckBonus", at = @At(value = "RETURN"), cancellable = true)
    private static void sinew$getFishingLuckBonus(ServerLevel level, ItemStack stack, Entity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof LivingEntity living && living.getAttribute(SinewAttributes.FISHING_LUCK) != null) {
            cir.setReturnValue(cir.getReturnValue() + (int) Math.round(living.getAttributeValue(SinewAttributes.FISHING_LUCK)));
        }
    }
}
