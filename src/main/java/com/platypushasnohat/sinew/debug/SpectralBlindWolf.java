package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.entity.spectral.SpectralSightSharing;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

//DEBUG
//tamable wolf that overrides tame sharing. even with goggles it can't target or hurt spectral mobs. however a normal tamed wolf can
public class SpectralBlindWolf extends Wolf implements SpectralSightSharing {

    public SpectralBlindWolf(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean sharesOwnerSight() {
        return false;
    }
}