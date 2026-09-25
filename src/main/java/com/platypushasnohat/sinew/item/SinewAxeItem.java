package com.platypushasnohat.sinew.item;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Locale;

public class SinewAxeItem extends AxeItem {

    public final ToolDefinition definition;

    public SinewAxeItem(ToolDefinition definition, Properties properties) {
        super(definition.tier(), properties);
        this.definition = definition;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        List<ItemAttributeModifiers.Entry> entries = modifiers.modifiers();
        EquipmentSlotGroup slot = EquipmentSlotGroup.MAINHAND;
        ResourceLocation location = Sinew.location("armor." + slot.name().toLowerCase(Locale.ROOT));
        for (ItemAttributeModifiers.Entry entry : entries) {
            builder.add(entry.attribute(), entry.modifier(), slot);
        }
        for (var entry : this.definition.attributes()) {
            builder.add(entry.attribute(), new AttributeModifier(location, entry.value(), entry.operation()), slot);
        }
        return builder.build();
    }
}