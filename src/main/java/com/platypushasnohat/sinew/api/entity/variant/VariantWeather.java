package com.platypushasnohat.sinew.api.entity.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum VariantWeather implements StringRepresentable {

    ANY("any"),
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    public static final Codec<VariantWeather> CODEC = StringRepresentable.fromEnum(VariantWeather::values);

    private final String name;

    VariantWeather(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}