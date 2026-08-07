package com.platypushasnohat.sinew.api.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class SinewMobVariant {

    public static final Codec<SinewMobVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
            ResourceLocation.CODEC.optionalFieldOf("baby_texture").forGetter(variant -> variant.babyTexture),
            RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(SinewMobVariant::biomes),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("weight", 1).forGetter(SinewMobVariant::weight),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(SinewMobVariant::priority),
            VariantTime.CODEC.optionalFieldOf("time", VariantTime.ANY).forGetter(SinewMobVariant::time),
            VariantWeather.CODEC.optionalFieldOf("weather", VariantWeather.ANY).forGetter(SinewMobVariant::weather),
            Codec.INT.optionalFieldOf("min_spawn_height").forGetter(SinewMobVariant::minSpawnHeight),
            Codec.INT.optionalFieldOf("max_spawn_height").forGetter(SinewMobVariant::maxSpawnHeight),
            Codec.STRING.optionalFieldOf("model_key").forGetter(SinewMobVariant::modelKey),
            VariantRenderType.CODEC.optionalFieldOf("render_type", VariantRenderType.ENTITY_CUTOUT).forGetter(SinewMobVariant::renderType)
    ).apply(instance, SinewMobVariant::new));

    private final ResourceLocation texture;
    private final Optional<ResourceLocation> babyTexture;
    private final Optional<HolderSet<Biome>> biomes;
    private final int weight;
    private final int priority;
    private final VariantTime time;
    private final VariantWeather weather;
    private final Optional<Integer> minSpawnHeight;
    private final Optional<Integer> maxSpawnHeight;
    private final Optional<String> modelKey;
    private final VariantRenderType renderType;

    public SinewMobVariant(ResourceLocation texture, Optional<ResourceLocation> babyTexture, Optional<HolderSet<Biome>> biomes, int weight, int priority, VariantTime time, VariantWeather weather, Optional<Integer> minSpawnHeight, Optional<Integer> maxSpawnHeight, Optional<String> modelKey, VariantRenderType renderType) {
        this.texture = texture;
        this.babyTexture = babyTexture;
        this.biomes = biomes;
        this.weight = weight;
        this.priority = priority;
        this.time = time;
        this.weather = weather;
        this.minSpawnHeight = minSpawnHeight;
        this.maxSpawnHeight = maxSpawnHeight;
        this.modelKey = modelKey;
        this.renderType = renderType;
    }

    public ResourceLocation texture() {
        return fullTextureId(this.texture);
    }

    public ResourceLocation babyTexture() {
        return this.babyTexture.map(SinewMobVariant::fullTextureId).orElse(this.texture);
    }

    public ResourceLocation rawTexture() {
        return this.texture;
    }

    public ResourceLocation rawBabyTexture() {
        return this.texture;
    }

    public Optional<HolderSet<Biome>> biomes() {
        return this.biomes;
    }

    public int weight() {
        return this.weight;
    }

    public int priority() {
        return this.priority;
    }

    public VariantTime time() {
        return this.time;
    }

    public VariantWeather weather() {
        return this.weather;
    }

    public Optional<Integer> minSpawnHeight() {
        return this.minSpawnHeight;
    }

    public Optional<Integer> maxSpawnHeight() {
        return this.maxSpawnHeight;
    }

    public Optional<String> modelKey() {
        return this.modelKey;
    }

    public VariantRenderType renderType() {
        return this.renderType;
    }

    public static ResourceLocation fullTextureId(ResourceLocation texture) {
        return texture.withPath(path -> "textures/" + path + ".png");
    }
}
