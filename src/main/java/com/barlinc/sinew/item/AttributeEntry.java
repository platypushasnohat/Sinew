package com.barlinc.sinew.item;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeEntry(Attribute attribute, double value, AttributeModifier.Operation operation) {}
