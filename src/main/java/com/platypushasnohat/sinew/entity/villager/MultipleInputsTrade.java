package com.platypushasnohat.sinew.entity.villager;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Optional;

public class MultipleInputsTrade implements VillagerTrades.ItemListing {

    public final ItemStack inputItem;
    public final int inputCount;
    public final int emeraldCost;
    public final ItemStack outputItem;
    public final int outputCount;
    public final int maxTrades;
    public final int experience;
    public final float priceMultiplier;

    public MultipleInputsTrade(ItemLike inputItem, int inputCount, int emeraldCost, Item outputItem, int outputCount, int maxTrades, int experience) {
        this.inputItem = new ItemStack(inputItem);
        this.inputCount = inputCount;
        this.emeraldCost = emeraldCost;
        this.outputItem = new ItemStack(outputItem);
        this.outputCount = outputCount;
        this.maxTrades = maxTrades;
        this.experience = experience;
        this.priceMultiplier = 0.05F;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource source) {
        return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost), Optional.of(new ItemCost(this.inputItem.getItem(), this.inputCount)), new ItemStack(this.outputItem.getItem(), this.outputCount), this.maxTrades, this.experience, this.priceMultiplier);
    }
}