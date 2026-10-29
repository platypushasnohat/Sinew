package com.platypushasnohat.sinew.registry;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import com.platypushasnohat.sinew.supporter.SupporterCape;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SinewAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Sinew.MOD_ID);

    //sources arent saved
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpectralSight>> SPECTRAL_SIGHT = ATTACHMENT_TYPES.register("spectral_sight", () -> AttachmentType.builder(() -> new SpectralSight()).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SupporterCape>> SUPPORTER_CAPE = ATTACHMENT_TYPES.register("selected_supporter_cape", () -> AttachmentType.builder(() -> SupporterCape.NONE).serialize(SupporterCape.CODEC).copyOnDeath().build());
}
