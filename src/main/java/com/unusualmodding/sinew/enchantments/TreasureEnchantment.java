package com.unusualmodding.sinew.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TreasureEnchantment extends SinewEnchantment {

    public TreasureEnchantment(String name, Rarity rarity, EnchantmentCategory category, int levels, int minXP, EquipmentSlot... equipmentSlot) {
        super(name, rarity, category, levels, minXP, equipmentSlot);
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }
}
