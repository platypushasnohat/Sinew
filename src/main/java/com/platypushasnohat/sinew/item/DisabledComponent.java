package com.platypushasnohat.sinew.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record DisabledComponent(ItemStack item, int time) {

    public static Codec<DisabledComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(DisabledComponent::item),
            Codec.INT.fieldOf("time").forGetter(DisabledComponent::time)
    ).apply(instance, DisabledComponent::new));

    public static StreamCodec<RegistryFriendlyByteBuf, DisabledComponent> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            disabledComponent -> disabledComponent.item,
            ByteBufCodecs.VAR_INT,
            disabledComponent -> disabledComponent.time,
            DisabledComponent::new
    );
}