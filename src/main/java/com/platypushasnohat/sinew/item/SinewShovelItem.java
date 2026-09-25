package com.platypushasnohat.sinew.item;

import net.minecraft.world.item.ShovelItem;

public class SinewShovelItem extends ShovelItem {

    public final ToolDefinition definition;

    public SinewShovelItem(ToolDefinition definition, float attackDamage, float attackSpeed, Properties properties) {
        super(definition.tier(), properties.attributes(SinewItemUtils.createTieredItemAttributes(definition, attackDamage, attackSpeed)));
        this.definition = definition;
    }
}