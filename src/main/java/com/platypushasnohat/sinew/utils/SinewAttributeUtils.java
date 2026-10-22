package com.platypushasnohat.sinew.utils;

import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class SinewAttributeUtils {

    public static void applyDamageResistance(LivingDamageEvent.Pre event, Holder<Attribute> attribute) {
        if (event.getEntity().getAttribute(attribute) != null) {
            float attributeValue = (float) Mth.clamp(event.getEntity().getAttributeValue(attribute), 0.0D, 1.0D);
            if (attributeValue > 0.0F) {
                event.setNewDamage(event.getNewDamage() * (1.0F - attributeValue));
            }
        }
    }
}
