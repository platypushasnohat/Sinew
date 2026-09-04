package com.platypushasnohat.sinew.entity.variant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

public class SinewMobVariantUtils {

    public static Optional<Holder.Reference<SinewMobVariant>> byId(HolderLookup.Provider provider, ResourceKey<Registry<SinewMobVariant>> registryKey, ResourceLocation id) {
        if (provider == null) {
            return Optional.empty();
        }
        return provider.lookup(registryKey).flatMap(lookup -> lookup.get(ResourceKey.create(registryKey, id)));
    }

    public static Optional<Holder.Reference<SinewMobVariant>> byKey(HolderLookup.Provider provider, ResourceKey<Registry<SinewMobVariant>> registryKey, ResourceKey<SinewMobVariant> key) {
        if (provider == null) {
            return Optional.empty();
        }
        return provider.lookup(registryKey).flatMap(lookup -> lookup.get(key));
    }

    public static int getVariantCount(HolderLookup.Provider provider, ResourceKey<Registry<SinewMobVariant>> registryKey) {
        if (provider == null) {
            return 0;
        }
        return provider.lookup(registryKey).map(lookup -> (int) lookup.listElements().count()).orElse(0);
    }

    public static Optional<Holder.Reference<SinewMobVariant>> selectVariantForSpawn(ServerLevelAccessor level, BlockPos pos, ResourceKey<Registry<SinewMobVariant>> registryKey) {
        Optional<Registry<SinewMobVariant>> maybeRegistry = level.registryAccess().registry(registryKey);
        if (maybeRegistry.isEmpty()) {
            return Optional.empty();
        }
        Registry<SinewMobVariant> registry = maybeRegistry.get();
        Holder<Biome> biome = level.getBiome(pos);

        List<Holder.Reference<SinewMobVariant>> pool = registry.holders()
                .filter(holder -> holder.value().weight() > 0)
                .filter(holder -> holder.value().biomes().map(biomes -> biomes.contains(biome)).orElse(false))
                .filter(holder -> getTime(holder.value().time(), level))
                .filter(holder -> getWeather(holder.value().weather(), level))
                .filter(holder -> getSpawnHeight(holder.value(), pos, level))
                .toList();

        if (pool.isEmpty()) {
            pool = registry.holders().filter(holder -> holder.value().weight() > 0 && holder.value().biomes().isEmpty()).toList();
        }
        return weightedChoice(highestPriority(pool), level.getRandom());
    }

    private static boolean getTime(VariantTime time, ServerLevelAccessor level) {
        return switch (time) {
            case ANY -> true;
            case DAY -> level.getLevel().isDay();
            case NIGHT -> level.getLevel().isNight();
        };
    }

    private static boolean getWeather(VariantWeather weather, ServerLevelAccessor level) {
        return switch (weather) {
            case ANY -> true;
            case CLEAR -> !level.getLevel().isRaining();
            case RAIN -> level.getLevel().isRaining() && !level.getLevel().isThundering();
            case THUNDER -> level.getLevel().isThundering();
        };
    }

    private static boolean getSpawnHeight(SinewMobVariant variant, BlockPos pos, ServerLevelAccessor level) {
        int y = pos.getY();
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight() - 1;
        return y >= variant.minSpawnHeight().orElse(minY) && y <= variant.maxSpawnHeight().orElse(maxY);
    }

    private static List<Holder.Reference<SinewMobVariant>> highestPriority(List<Holder.Reference<SinewMobVariant>> pool) {
        int max = pool.stream().mapToInt(holder -> holder.value().priority()).max().orElse(0);
        return pool.stream().filter(holder -> holder.value().priority() == max).toList();
    }

    private static Optional<Holder.Reference<SinewMobVariant>> weightedChoice(List<Holder.Reference<SinewMobVariant>> pool, RandomSource random) {
        int total = pool.stream().mapToInt(holder -> holder.value().weight()).sum();
        if (total <= 0) {
            return Optional.empty();
        }
        int roll = random.nextInt(total);
        for (Holder.Reference<SinewMobVariant> holder : pool) {
            roll -= holder.value().weight();
            if (roll < 0) {
                return Optional.of(holder);
            }
        }
        return Optional.empty();
    }
}
