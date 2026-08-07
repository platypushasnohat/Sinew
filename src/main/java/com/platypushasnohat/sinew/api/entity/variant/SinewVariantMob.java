package com.platypushasnohat.sinew.api.entity.variant;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public interface SinewVariantMob {

    String VARIANT_TAG = "Variant";

    default ResourceKey<Registry<SinewMobVariant>> variantRegistryKey() {
        return SinewMobVariantRegistry.registryFor(((Mob) this).getType());
    }

    default ResourceKey<SinewMobVariant> defaultVariant() {
        return SinewMobVariantRegistry.defaultFor(((Mob) this).getType());
    }

    ResourceLocation fallbackVariantTexture();

    String getVariantRawId();

    void setVariantRawId(String id);

    default ResourceLocation getVariantId() {
        ResourceLocation id = ResourceLocation.tryParse(this.getVariantRawId());
        return id != null ? id : this.defaultVariant().location();
    }

    default void setVariant(Holder<SinewMobVariant> variant) {
        variant.unwrapKey().ifPresent(key -> this.setVariantRawId(key.location().toString()));
    }

    default Optional<Holder.Reference<SinewMobVariant>> getVariantHolder() {
        return SinewMobVariantUtils.byId(((Mob) this).level().registryAccess(), this.variantRegistryKey(), this.getVariantId());
    }

    default boolean hasNonDefaultVariant() {
        return !this.getVariantId().equals(this.defaultVariant().location());
    }

    default ResourceLocation getVariantTexture() {
        return this.getVariantHolder().map(holder -> holder.value().texture()).orElseGet(this::fallbackVariantTexture);
    }
    default ResourceLocation getVariantTextureRaw() {
        return this.getVariantHolder().map(holder -> holder.value().rawTexture()).orElseGet(this::fallbackVariantTexture);
    }

    default VariantRenderType getVariantRenderType() {
        return this.getVariantHolder().map(holder -> holder.value().renderType()).orElse(VariantRenderType.ENTITY_CUTOUT);
    }

    default void saveVariant(CompoundTag tag) {
        tag.putString(VARIANT_TAG, this.getVariantId().toString());
    }

    default void loadVariant(CompoundTag compoundTag) {
        if (compoundTag.contains(VARIANT_TAG)) {
            this.setVariantRawId(compoundTag.getString(VARIANT_TAG));
        }
    }

    default void pickVariantForSpawn(ServerLevelAccessor level) {
        SinewMobVariantUtils.selectVariantForSpawn(level, ((Mob) this).blockPosition(), this.variantRegistryKey()).ifPresent(this::setVariant);
    }
}
