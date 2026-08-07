package com.platypushasnohat.sinew.api.entity.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum VariantRenderType implements StringRepresentable {

    ENTITY_CUTOUT("entity_cutout"),
    ENTITY_CUTOUT_NO_CULL("entity_cutout_no_cull"),
    ENTITY_CUTOUT_MIPPED("entity_cutout_mipped"),
    ENTITY_TRANSLUCENT("entity_translucent"),
    ENTITY_TRANSLUCENT_CULL("entity_translucent_cull"),
    ENTITY_TRANSLUCENT_EMISSIVE("entity_translucent_emissive");

    public static final Codec<VariantRenderType> CODEC = StringRepresentable.fromEnum(VariantRenderType::values);

    private final String name;

    VariantRenderType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}