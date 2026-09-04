package com.platypushasnohat.sinew.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.function.Supplier;

public abstract class SinewSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public final String modId;

    public SinewSoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
        this.modId = modId;
    }

    public void registerSoundDefinition(Supplier<SoundEvent> soundEvent, String subtitle, SoundDefinition.Sound... sounds) {
        this.add(soundEvent.get(), SoundDefinition.definition().subtitle("subtitles." + this.modId + "." + subtitle).with(sounds));
    }

    public void registerSound(Supplier<SoundEvent> soundEvent, SoundDefinition.Sound... sounds){
        this.registerSoundDefinition(soundEvent, soundEvent.get().getLocation().getPath(), sounds);
    }
}
