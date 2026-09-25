package com.platypushasnohat.sinew.item;

import net.minecraft.world.item.PickaxeItem;

public class SinewPickaxeItem extends PickaxeItem {

    public final ToolDefinition definition;

    public SinewPickaxeItem(ToolDefinition definition, float attackDamage, float attackSpeed, Properties properties) {
        super(definition.tier(), properties.attributes(SinewItemUtils.createTieredItemAttributes(definition, attackDamage, attackSpeed)));
        this.definition = definition;
    }
}