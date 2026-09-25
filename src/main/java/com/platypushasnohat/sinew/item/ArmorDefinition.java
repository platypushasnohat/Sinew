package com.platypushasnohat.sinew.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ArmorDefinition {

    private final Holder<ArmorMaterial> material;
    private final List<AttributeEntry> attributes;
    private final Function<EquipmentSlot, ResourceLocation> textureFunction;
    private final boolean walkOnPowderedSnow;

    private ArmorDefinition(Holder<ArmorMaterial> material, List<AttributeEntry> attributes, Function<EquipmentSlot, ResourceLocation> textureFunction, boolean walkOnPowderedSnow) {
        this.material = material;
        this.attributes = attributes;
        this.textureFunction = textureFunction;
        this.walkOnPowderedSnow = walkOnPowderedSnow;
    }

    public Holder<ArmorMaterial> material() {
        return this.material;
    }

    public List<AttributeEntry> attributes() {
        return this.attributes;
    }

    public Function<EquipmentSlot, ResourceLocation> textureFunction() {
        return this.textureFunction;
    }

    public boolean canWalkOnPowderedSnow() {
        return this.walkOnPowderedSnow;
    }

    public static class Builder {

        private Holder<ArmorMaterial> material;
        private final List<AttributeEntry> attributes = new ArrayList<>();
        private Function<EquipmentSlot, ResourceLocation> textureFunction = s -> null;
        private boolean walkOnPowderedSnow = false;

        public Builder material(Holder<ArmorMaterial> material) {
            this.material = material;
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            this.attributes.add(new AttributeEntry(attribute, value, operation));
            return this;
        }

        public Builder texture(Function<EquipmentSlot, ResourceLocation> function) {
            this.textureFunction = function;
            return this;
        }

        public Builder walkOnPowderedSnow() {
            this.walkOnPowderedSnow = true;
            return this;
        }

        public ArmorDefinition build() {
            Objects.requireNonNull(this.material, "Armor material must not be null");
            return new ArmorDefinition(this.material, List.copyOf(this.attributes), this.textureFunction, this.walkOnPowderedSnow);
        }
    }
}