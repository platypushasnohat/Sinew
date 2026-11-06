package com.platypushasnohat.sinew.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class BaseIdAttribute extends RangedAttribute {

    private final ResourceLocation baseId;

    public BaseIdAttribute(String descriptionId, double defaultValue, double min, double max, ResourceLocation baseId) {
        super(descriptionId, defaultValue, min, max);
        this.baseId = baseId;
    }

    @Override
    public ResourceLocation getBaseId() {
        return this.baseId;
    }
}