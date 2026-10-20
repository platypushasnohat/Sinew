package com.platypushasnohat.sinew.registry;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.item.DisabledComponent;
import com.platypushasnohat.sinew.item.ItemAnimations;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class SinewDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Sinew.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MAX_POWER = register("max_power", builder -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> POWER = register("power", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DisabledComponent>> DISABLED = register("disabled", builder -> builder.persistent(DisabledComponent.CODEC).networkSynchronized(DisabledComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemAnimations>> ITEM_ANIMATIONS = register("item_animations", builder -> builder.networkSynchronized(ItemAnimations.STREAM_CODEC));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
