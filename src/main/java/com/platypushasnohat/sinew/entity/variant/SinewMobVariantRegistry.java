package com.platypushasnohat.sinew.entity.variant;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
public class SinewMobVariantRegistry {

    public static final Set<ResourceKey<Registry<SinewMobVariant>>> REGISTRIES = new HashSet<>();

    public static ResourceKey<Registry<SinewMobVariant>> register(String namespace, String name) {
        ResourceKey<Registry<SinewMobVariant>> key = registryFor(ResourceLocation.fromNamespaceAndPath(namespace, name));
        REGISTRIES.add(key);
        return key;
    }

    public static void registerVariantRegistries(DataPackRegistryEvent.NewRegistry event) {
        REGISTRIES.forEach(registry -> event.dataPackRegistry(registry, SinewMobVariant.DIRECT_CODEC, SinewMobVariant.DIRECT_CODEC));
    }

    public static ResourceKey<Registry<SinewMobVariant>> registryFor(ResourceLocation entityId) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(entityId.getNamespace(), "mob_variant/" + entityId.getPath()));
    }

    public static ResourceKey<Registry<SinewMobVariant>> registryFor(EntityType<?> type) {
        return registryFor(EntityType.getKey(type));
    }

    public static Optional<ResourceKey<Registry<SinewMobVariant>>> findRegistry(EntityType<?> type) {
        ResourceKey<Registry<SinewMobVariant>> registry = registryFor(type);
        return REGISTRIES.contains(registry) ? Optional.of(registry) : Optional.empty();
    }

    public static ResourceKey<SinewMobVariant> defaultFor(EntityType<?> type) {
        ResourceLocation id = EntityType.getKey(type);
        return createKey(registryFor(id), id.getPath());
    }

    public static ResourceKey<SinewMobVariant> createKey(ResourceKey<Registry<SinewMobVariant>> registry, String name) {
        return ResourceKey.create(registry, ResourceLocation.fromNamespaceAndPath(registry.location().getNamespace(), name));
    }
}
