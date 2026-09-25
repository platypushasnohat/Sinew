package com.platypushasnohat.sinew.item;

import net.minecraft.world.item.AxeItem;

public class SinewAxeItem extends AxeItem {

    public final ToolDefinition definition;

    public SinewAxeItem(ToolDefinition definition, float attackDamage, float attackSpeed, Properties properties) {
        super(definition.tier(), properties.attributes(SinewItemUtils.createTieredItemAttributes(definition, attackDamage, attackSpeed)));
        this.definition = definition;
    }
}