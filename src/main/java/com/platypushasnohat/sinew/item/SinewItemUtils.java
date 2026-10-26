package com.platypushasnohat.sinew.item;

import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    public static boolean shouldConsumeAmmo(LivingEntity shooter, ItemStack weapon) {
        boolean consume = true;
        if (shooter != null && shooter.getAttribute(SinewAttributes.AMMO_CONSERVATION) != null) {
            double saveChance = shooter.getAttributeValue(SinewAttributes.AMMO_CONSERVATION);
            consume = saveChance <= 0.0D || shooter.getRandom().nextDouble() >= saveChance;
        }
        return consume;
    }

    public static int getAmmoToConsume(LivingEntity shooter, ItemStack weapon, int amount) {
        int consumed = 0;
        for (int i = 0; i < amount; i++) {
            if (shouldConsumeAmmo(shooter, weapon)) {
                consumed++;
            }
        }
        return consumed;
    }

    public static boolean shouldConsumeMana(LivingEntity caster, ItemStack weapon) {
        boolean consume = true;
        if (caster != null && caster.getAttribute(SinewAttributes.MANA_CONSERVE_CHANCE) != null) {
            double conserveChance = caster.getAttributeValue(SinewAttributes.MANA_CONSERVE_CHANCE);
            consume = conserveChance <= 0.0D || caster.getRandom().nextDouble() >= conserveChance;
        }
        return consume;
    }
}
