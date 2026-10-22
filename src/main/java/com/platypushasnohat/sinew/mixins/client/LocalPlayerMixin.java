package com.platypushasnohat.sinew.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.platypushasnohat.sinew.item.MovementModifyingItem;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private boolean sinew$canPlayerSprintWhileUsingItem(LocalPlayer player) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof MovementModifyingItem movementModifyingItem) {
            return movementModifyingItem.canSprintWhileUsing();
        }
        return false;
    }

    @Unique
    private float sinew$getUsedItemMovementMultiplier(LocalPlayer player, float value) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof MovementModifyingItem movementModifyingItem) {
            return movementModifyingItem.getUseMovementMultiplier();
        }
        return value;
    }

    @WrapOperation(method = {"aiStep", "canStartSprinting"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean sinew$canStartSprinting(LocalPlayer player, Operation<Boolean> original) {
        if (original.call(player)) {
            return !this.sinew$canPlayerSprintWhileUsingItem(player);
        }
        return false;
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F, ordinal = 0))
    private float sinew$moveFasterWhileUsingItemLeft(float value) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        return this.sinew$getUsedItemMovementMultiplier(player, value);
    }

    @ModifyConstant(method = "aiStep", constant = @Constant(floatValue = 0.2F, ordinal = 1))
    private float sinew$moveFasterWhileUsingItemForward(float value) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        return this.sinew$getUsedItemMovementMultiplier(player, value);
    }
}