package com.unusualmodding.sinew.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SinewEnchantment extends Enchantment {

    private int levels;
    private int minXP;
    private String registryName;

    public SinewEnchantment(String name, Rarity rarity, EnchantmentCategory category, int levels, int minXP, EquipmentSlot... equipmentSlot) {
        super(rarity, category, equipmentSlot);
        this.levels = levels;
        this.minXP = minXP;
        this.registryName = name;
    }

    @Override
    public int getMinCost(int i) {
        return 1 + (i - 1) * minXP;
    }

    @Override
    public int getMaxCost(int i) {
        return super.getMinCost(i) + 30;
    }

    @Override
    public int getMaxLevel() {
        return levels;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        return this != enchantment && areCompatible(this, enchantment);
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    public String getName(){
        return registryName;
    }

    public static boolean areCompatible(SinewEnchantment enchantment1, Enchantment enchantment2) {
        return true;
    }
}
