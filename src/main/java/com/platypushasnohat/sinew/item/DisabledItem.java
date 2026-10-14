package com.platypushasnohat.sinew.item;

import com.platypushasnohat.sinew.registry.SinewDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DisabledItem extends Item {

    public static final int MAX_TIME = 200;

    public DisabledItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof ServerPlayer player) {
            DisabledComponent disabledComponent = stack.get(SinewDataComponents.DISABLED);
            if (disabledComponent != null) {
                int disabledTime = getDisabledTime(stack);
                setDisabledTime(stack, disabledTime + 1);
                if (disabledTime >= disabledComponent.time()) {
                    enableItem(player, slotId);
                    setDisabledTime(stack, 0);
                    return;
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    public static void disableItem(ServerPlayer player, int slot, Supplier<Item> disabledItem) {
        Inventory inventory = player.getInventory();
        ItemStack disabled = disabledItem.get().getDefaultInstance();
        disabled.set(SinewDataComponents.DISABLED, new DisabledComponent(inventory.getItem(slot), MAX_TIME));
        inventory.setItem(slot, disabled);
    }

    public static void enableItem(ServerPlayer player, int slot) {
        Inventory inventory = player.getInventory();
        ItemStack disabledItem = inventory.getItem(slot);
        DisabledComponent disabledComponent = disabledItem.get(SinewDataComponents.DISABLED);
        if (disabledComponent != null) {
            ItemStack original = disabledComponent.item();
            if (!original.isEmpty()) {
                inventory.setItem(slot, original);
            }
        }
    }

    public static int getDisabledTime(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return compoundtag.getInt("DisabledTime");
    }

    public static void setDisabledTime(ItemStack stack, int disabledTime) {
        CompoundTag compoundtag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        compoundtag.putInt("DisabledTime", disabledTime);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
    }
}