package com.platypushasnohat.sinew.entity.utils;

import net.minecraft.util.Mth;

public class BodyChain {

    private final float[] yawGains;
    private final float[] pitchGains;

    private final float[] prevSegmentYaw;
    private final float[] segmentYaw;

    private final float[] prevSegmentPitch;
    private final float[] segmentPitch;

    private float prevRenderYaw;
    private float renderYaw;

    private boolean initialized;

    public BodyChain(float[] yawGains, float[] pitchGains) {
        this.yawGains = yawGains;
        this.pitchGains = pitchGains;
        this.segmentYaw = new float[yawGains.length];
        this.prevSegmentYaw = new float[yawGains.length];
        this.segmentPitch = new float[pitchGains.length];
        this.prevSegmentPitch = new float[pitchGains.length];
    }

    public void tick(float bodyYaw, float bodyPitch, float targetPitch) {
        if (!this.initialized) {
            this.initialized = true;
            this.renderYaw = this.prevRenderYaw = bodyYaw;
            for (int i = 0; i < this.segmentYaw.length; i++) {
                this.segmentYaw[i] = this.prevSegmentYaw[i] = bodyYaw;
            }
        }
        this.prevRenderYaw = this.renderYaw;
        System.arraycopy(this.segmentYaw, 0, this.prevSegmentYaw, 0, this.segmentYaw.length);
        System.arraycopy(this.segmentPitch, 0, this.prevSegmentPitch, 0, this.segmentPitch.length);

        this.renderYaw += Mth.wrapDegrees(bodyYaw - this.renderYaw) * 0.2F;
        for (int i = 0; i < this.segmentYaw.length; i++) {
            float yawReference = i == 0 ? bodyYaw : i == 1 ? this.renderYaw : this.segmentYaw[i - 1];
            this.segmentYaw[i] += Mth.wrapDegrees(yawReference - this.segmentYaw[i]) * this.yawGains[i];
            float pitchReference = i == 0 ? targetPitch : i == 1 ? bodyPitch : this.segmentPitch[i - 1];
            this.segmentPitch[i] += (pitchReference - this.segmentPitch[i]) * this.pitchGains[i];
        }
    }

    public float getRenderYaw(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.prevRenderYaw, this.renderYaw);
    }

    public float getSegmentYawOffset(int index, float partialTicks) {
        float current = Mth.rotLerp(partialTicks, this.prevSegmentYaw[index], this.segmentYaw[index]);
        float reference = index <= 1 ? this.getRenderYaw(partialTicks) : Mth.rotLerp(partialTicks, this.prevSegmentYaw[index - 1], this.segmentYaw[index - 1]);
        return Mth.wrapDegrees(current - reference);
    }

    public float getSegmentPitchOffset(int index, float partialTicks, float bodyPitch) {
        float current = Mth.lerp(partialTicks, this.prevSegmentPitch[index], this.segmentPitch[index]);
        float reference = index <= 1 ? bodyPitch : Mth.lerp(partialTicks, this.prevSegmentPitch[index - 1], this.segmentPitch[index - 1]);
        return current - reference;
    }
}