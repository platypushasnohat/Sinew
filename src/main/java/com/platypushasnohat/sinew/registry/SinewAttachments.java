package com.platypushasnohat.sinew.registry;

import com.mojang.serialization.Codec;
import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SinewAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Sinew.MOD_ID);

    //sources arent saved
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpectralSight>> SPECTRAL_SIGHT = ATTACHMENT_TYPES.register("spectral_sight", () -> AttachmentType.builder(() -> new SpectralSight()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SUPPORTER_CAPE = ATTACHMENT_TYPES.register("supporter_cape", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());
}
