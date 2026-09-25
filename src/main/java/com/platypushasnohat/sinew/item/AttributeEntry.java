package com.platypushasnohat.sinew.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.platypushasnohat.sinew.Sinew;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;

public record AttributeEntry(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {

    public static final MapCodec<AttributeEntry> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEntry::attribute),
                            Codec.DOUBLE.fieldOf("value").forGetter(AttributeEntry::value),
                            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeEntry::operation)
                    )
                    .apply(instance, AttributeEntry::new)
    );

    public static final Codec<AttributeEntry> CODEC = MAP_CODEC.codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, AttributeEntry> STREAM_CODEC = StreamCodec.composite(
            Attribute.STREAM_CODEC,
            AttributeEntry::attribute,
            ByteBufCodecs.DOUBLE,
            AttributeEntry::value,
            AttributeModifier.Operation.STREAM_CODEC,
            AttributeEntry::operation,
            AttributeEntry::new
    );

    public CompoundTag save() {
        DataResult<Tag> result = CODEC.encode(this, NbtOps.INSTANCE, new CompoundTag());
        return (CompoundTag) result.getOrThrow();
    }

    @Nullable
    public static AttributeEntry load(CompoundTag compoundTag) {
        DataResult<AttributeEntry> result = CODEC.parse(NbtOps.INSTANCE, compoundTag);
        if (result.isSuccess()) {
            return result.getOrThrow();
        } else {
            Sinew.LOGGER.warn("Unable to create attribute entry: {}", result.error().get().message());
            return null;
        }
    }
}