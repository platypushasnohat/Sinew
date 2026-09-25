package com.platypushasnohat.sinew.entity.villager;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;

public class SingleInputTrade implements VillagerTrades.ItemListing {

    public final ItemStack sellingItem;
    public final int emeraldCount;
    public final int sellingItemCount;
    public final int maxUses;
    public final int xpValue;
    public final float priceMultiplier;

    public SingleInputTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
        this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
    }

    public SingleInputTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
        this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
    }

    public SingleInputTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
        this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
    }

    public SingleInputTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
        this(sellingItem, emeraldCount, sellingItemCount, maxUses, xpValue, 0.05F);
    }

    public SingleInputTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue, float priceMultiplier) {
        this.sellingItem = sellingItem;
        this.emeraldCount = emeraldCount;
        this.sellingItemCount = sellingItemCount;
        this.maxUses = maxUses;
        this.xpValue = xpValue;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(Entity trader, RandomSource random) {
        return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
    }
}