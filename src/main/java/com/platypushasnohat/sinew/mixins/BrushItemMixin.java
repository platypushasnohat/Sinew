package com.platypushasnohat.sinew.mixins;

import com.platypushasnohat.sinew.block.Brushable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public class BrushItemMixin {

    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;", shift = At.Shift.AFTER))
    private void sinew$brushableBrush(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration, CallbackInfo ci) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            if (player.pick(player.blockInteractionRange(), 0.0F, false) instanceof BlockHitResult hitResult) {
                BlockPos pos = hitResult.getBlockPos();
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof Brushable brushable) {
                    boolean brush = brushable.brush(level.getGameTime());
                    if (brush) {
                        EquipmentSlot slot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                        stack.hurtAndBreak(1, livingEntity, slot);
                    }
                }
            }
        }
    }
}