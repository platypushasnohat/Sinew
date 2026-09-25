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

public class SellItemTrade implements VillagerTrades.ItemListing {

    public final Item tradeItem;
    public final int count;
    public final int maxUses;
    public final int xpValue;
    public final float priceMultiplier;

    public SellItemTrade(ItemLike item, int count, int maxUses, int xpValue) {
        this.tradeItem = item.asItem();
        this.count = count;
        this.maxUses = maxUses;
        this.xpValue = xpValue;
        this.priceMultiplier = 0.05F;
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource source) {
        ItemCost cost = new ItemCost(this.tradeItem, 1);
        return new MerchantOffer(cost, new ItemStack(Items.EMERALD, this.count), this.maxUses, this.xpValue, this.priceMultiplier);
    }
}