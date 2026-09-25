package com.platypushasnohat.sinew.item;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;

import java.util.ArrayList;
import java.util.List;

public class ToolDefinition {

    private final Tier tier;
    private final List<AttributeEntry> attributes;

    private ToolDefinition(Tier tier, List<AttributeEntry> attributes) {
        this.tier = tier;
        this.attributes = attributes;
    }

    public Tier tier() {
        return this.tier;
    }

    public List<AttributeEntry> attributes() {
        return this.attributes;
    }

    public static class Builder {

        private Tier tier;
        private final List<AttributeEntry> attributes = new ArrayList<>();

        public Builder tier(Tier tier) {
            this.tier = tier;
            return this;
        }

        public Builder attribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            this.attributes.add(new AttributeEntry(attribute, value, operation));
            return this;
        }

        public ToolDefinition build() {
            return new ToolDefinition(this.tier, List.copyOf(this.attributes));
        }
    }
}