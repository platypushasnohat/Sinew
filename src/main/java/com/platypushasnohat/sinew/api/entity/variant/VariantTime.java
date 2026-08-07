package com.platypushasnohat.sinew.api.entity.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum VariantTime implements StringRepresentable {

    ANY("any"),
    DAY("day"),
    NIGHT("night");

    public static final Codec<VariantTime> CODEC = StringRepresentable.fromEnum(VariantTime::values);

    private final String name;

    VariantTime(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}