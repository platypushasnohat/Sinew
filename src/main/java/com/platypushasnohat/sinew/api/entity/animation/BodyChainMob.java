package com.platypushasnohat.sinew.api.entity.animation;

public interface BodyChainMob {

    BodyChain getBodyChain();

    float getRenderYaw(float partialTicks);

    float getSegmentYawOffset(int index, float partialTicks);

    float getSegmentPitchOffset(int index, float partialTicks);
}
