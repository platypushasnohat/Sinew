package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantedCountIncreaseFunction.class)
public class EnchantedCountIncreaseFunctionMixin {

    @Shadow
    @Final
    private Holder<Enchantment> enchantment;

    @ModifyVariable(method = "run", at = @At(value = "STORE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I", ordinal = 0), index = 5)
    private int sinew$getLootingLevel(int enchantmentLevel, ItemStack stack, LootContext lootContext) {
        if (this.enchantment.is(Enchantments.LOOTING) && lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof Player player && player.getAttribute(SinewAttributes.LOOTING) != null) {
            return enchantmentLevel + (int) Math.round(player.getAttributeValue(SinewAttributes.LOOTING));
        }
        return enchantmentLevel;
    }
}
