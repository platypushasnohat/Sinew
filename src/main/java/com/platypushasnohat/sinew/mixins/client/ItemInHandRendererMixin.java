package com.platypushasnohat.sinew.mixins.client;

import com.platypushasnohat.sinew.item.DisabledItem;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(method = "renderArmWithItem", at = @At(value = "HEAD"), index = 6, argsOnly = true)
    protected ItemStack sinew$renderArmWithItem(ItemStack stack) {
        if (stack.getItem() instanceof DisabledItem) {
            return ItemStack.EMPTY;
        }
        return stack;
    }
}