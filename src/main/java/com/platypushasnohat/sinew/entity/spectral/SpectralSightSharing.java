package com.platypushasnohat.sinew.entity.spectral;

//implement on a tamed or owned mob to override whether it shares its owner's spectral sight
//owned mobs share it by default, see SpectralSight.canPerceive
public interface SpectralSightSharing {

    //false = this mob only uses its own sight sources
    default boolean sharesOwnerSight() {
        return true;
    }
}