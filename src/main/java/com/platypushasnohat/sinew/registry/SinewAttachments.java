package com.platypushasnohat.sinew.registry;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class SinewAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Sinew.MOD_ID);

    //sources arent saved. insanity rechecks every second, and equipment looks at its change event on relog.
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpectralSight>> SPECTRAL_SIGHT = ATTACHMENT_TYPES.register("spectral_sight", () -> AttachmentType.builder(() -> new SpectralSight()).build());
}
