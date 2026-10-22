package com.platypushasnohat.sinew.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.platypushasnohat.sinew.item.SinewItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    @ModifyExpressionValue(method = "useAmmo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"))
    private static int sinew$useAmmo(int ammoUse, ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangible) {
        return ammoUse > 0 && !SinewItemUtils.shouldConsumeAmmo(shooter, weapon) ? 0 : ammoUse;
    }
}