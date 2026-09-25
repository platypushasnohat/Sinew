package com.platypushasnohat.sinew.item;

import net.minecraft.world.item.SwordItem;

public class SinewSwordItem extends SwordItem {

    public final ToolDefinition definition;

    public SinewSwordItem(ToolDefinition definition, float attackDamage, float attackSpeed, Properties properties) {
        super(definition.tier(), properties.attributes(SinewItemUtils.createTieredItemAttributes(definition, attackDamage, attackSpeed)));
        this.definition = definition;
    }
}