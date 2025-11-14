package com.unusualmodding.sinew.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ConfigurableArmorItem extends ArmorItem {

    private final ArmorDefinition definition;
    private boolean hasSetBonus;

    public ConfigurableArmorItem(Type type, Properties properties, ArmorDefinition definition) {
        super(definition.material(), type, properties);
        this.definition = definition;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != getEquipmentSlot()) return super.getAttributeModifiers(slot, stack);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));

        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(this.type);
        for (var entry : definition.attributes()) {
            builder.put(entry.attribute(), new AttributeModifier(uuid, entry.attribute().getDescriptionId(), entry.value(), entry.operation()));
        }
        return builder.build();
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return definition.canWalkOnPowderedSnow();
    }


    //todo fix this
    //@OnlyIn(Dist.CLIENT)
    //@Override
    //public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    //    consumer.accept((IClientItemExtensions) OpposingForce.PROXY.getArmorRenderProperties());
    //}

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return definition.textureFunction().apply(slot);
    }
}