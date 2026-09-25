package com.platypushasnohat.sinew.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SinewItemUtils {

    public static final ResourceLocation MODIFIERS = ResourceLocation.withDefaultNamespace("sinew_item_modifiers");

    public static ItemAttributeModifiers createTieredItemAttributes(ToolDefinition definition, float attackDamage, float attackSpeed) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (var entry : definition.attributes()) {
            builder.add(entry.attribute(), new AttributeModifier(MODIFIERS, entry.value(), entry.operation()), EquipmentSlotGroup.MAINHAND);
        }
        return builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage + definition.tier().getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }
}
