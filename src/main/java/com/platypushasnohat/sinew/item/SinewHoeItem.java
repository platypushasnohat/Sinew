package com.platypushasnohat.sinew.item;

import net.minecraft.world.item.HoeItem;

public class SinewHoeItem extends HoeItem {

    public final ToolDefinition definition;

    public SinewHoeItem(ToolDefinition definition, float attackDamage, float attackSpeed, Properties properties) {
        super(definition.tier(), properties.attributes(SinewItemUtils.createTieredItemAttributes(definition, attackDamage, attackSpeed)));
        this.definition = definition;
    }
}