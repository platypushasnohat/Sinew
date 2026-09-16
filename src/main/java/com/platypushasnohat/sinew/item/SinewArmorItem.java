package com.platypushasnohat.sinew.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class SinewArmorItem extends ArmorItem {

    public final ArmorDefinition definition;

    public SinewArmorItem(Type type, Properties properties, ArmorDefinition definition) {
        super(definition.material(), type, properties);
        this.definition = definition;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        List<ItemAttributeModifiers.Entry> entries = modifiers.modifiers();
        ResourceLocation location = ResourceLocation.withDefaultNamespace("armor." + this.type.getName());

        for (ItemAttributeModifiers.Entry entry : entries) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        for (var entry : this.definition.attributes()) {
            builder.add(entry.attribute(), new AttributeModifier(location, entry.value(), entry.operation()), entry.slot());
        }
        return builder.build();
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity entity) {
        return this.definition.canWalkOnPowderedSnow();
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return this.definition.textureFunction().apply(slot);
    }
}