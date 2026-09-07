package com.platypushasnohat.sinew.mixins.client;

import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {

    @Accessor("tickingSounds")
    List<TickableSoundInstance> getTickingSounds();

    @Accessor("queuedTickableSounds")
    List<TickableSoundInstance> getQueuedTickableSounds();
}