package com.platypushasnohat.sinew.supporter;

import com.mojang.serialization.Codec;
import com.platypushasnohat.sinew.Sinew;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum SupporterCape implements StringRepresentable {
    NONE("none", null),
    SUPPORTER("supporter", Sinew.location("textures/entity/barl_supporter_cape.png")),
    TARANTULA("tarantula", Sinew.location("textures/entity/tarantula_cape.png"));

    public static final Codec<SupporterCape> CODEC = StringRepresentable.fromEnum(SupporterCape::values);
    public static final StreamCodec<ByteBuf, SupporterCape> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO), Enum::ordinal);

    private final String name;
    private final ResourceLocation texture;

    SupporterCape(String name, ResourceLocation texture) {
        this.name = name;
        this.texture = texture;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public Component getDisplayName() {
        return this == NONE ? CommonComponents.OPTION_OFF : Component.translatable("options.sinew.supporterCape." + this.name);
    }

    public boolean canUse(UUID uuid) {
        return switch (this) {
            case NONE -> true;
            case SUPPORTER -> SupporterCapes.isSupporter(uuid);
            case TARANTULA -> SupporterList.TARANTULA.contains(uuid);
        };
    }

    public static List<SupporterCape> getAvailable(UUID uuid) {
        return Arrays.stream(values()).filter(cape -> cape.canUse(uuid)).toList();
    }

    public static SupporterCape byName(String name) {
        SupporterCape cape = Arrays.stream(values()).filter(value -> value.name.equals(name)).findFirst().orElse(NONE);
        if (name.equals("true")) {
            cape = SUPPORTER;
        }
        return cape;
    }
}
