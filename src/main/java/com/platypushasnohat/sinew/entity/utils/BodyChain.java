package com.platypushasnohat.sinew.entity.utils;

import net.minecraft.util.Mth;

public class BodyChain {

    public final float renderYawEase;
    public final float[] yawGains;
    public final float[] pitchGains;
    public final float rollPerYaw;
    public final float maxRoll;
    public final float rollEase;

    public final float[] prevSegmentYaw;
    public final float[] segmentYaw;
    public final float[] prevSegmentPitch;
    public final float[] segmentPitch;
    public float prevRenderYaw;
    public float renderYaw;
    public float prevRoll;
    public float roll;
    public boolean initialized;

    public BodyChain(float renderYawEase, float rollPerYaw, float maxRoll, float rollEase, float[] yawGains, float[] pitchGains) {
        this.renderYawEase = renderYawEase;
        this.yawGains = yawGains;
        this.pitchGains = pitchGains;
        this.rollPerYaw = rollPerYaw;
        this.maxRoll = maxRoll;
        this.rollEase = rollEase;
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
        this.prevRoll = this.roll;
        System.arraycopy(this.segmentYaw, 0, this.prevSegmentYaw, 0, this.segmentYaw.length);
        System.arraycopy(this.segmentPitch, 0, this.prevSegmentPitch, 0, this.segmentPitch.length);

        this.renderYaw += Mth.wrapDegrees(bodyYaw - this.renderYaw) * this.renderYawEase;
        for (int i = 0; i < this.segmentYaw.length; i++) {
            float yawReference = i == 0 ? bodyYaw : i == 1 ? this.renderYaw : this.segmentYaw[i - 1];
            this.segmentYaw[i] += Mth.wrapDegrees(yawReference - this.segmentYaw[i]) * this.yawGains[i];
            float pitchReference = i == 0 ? targetPitch : i == 1 ? bodyPitch : this.segmentPitch[i - 1];
            this.segmentPitch[i] += (pitchReference - this.segmentPitch[i]) * this.pitchGains[i];
        }

        float rollTarget = Mth.clamp(-Mth.wrapDegrees(this.renderYaw - this.prevRenderYaw) * this.rollPerYaw, -this.maxRoll, this.maxRoll);
        this.roll += (rollTarget - this.roll) * this.rollEase;
    }

    public float getRenderYaw() {
        return this.renderYaw;
    }

    public float getRenderYaw(float partialTick) {
        return Mth.rotLerp(partialTick, this.prevRenderYaw, this.renderYaw);
    }

    public float getRoll(float partialTick) {
        return Mth.lerp(partialTick, this.prevRoll, this.roll);
    }

    public float getSegmentYawOffset(int index, float partialTick) {
        float current = Mth.rotLerp(partialTick, this.prevSegmentYaw[index], this.segmentYaw[index]);
        float reference = index <= 1 ? this.getRenderYaw(partialTick) : Mth.rotLerp(partialTick, this.prevSegmentYaw[index - 1], this.segmentYaw[index - 1]);
        return Mth.wrapDegrees(current - reference);
    }

    public float getSegmentPitchOffset(int index, float partialTick, float bodyPitch) {
        float current = Mth.lerp(partialTick, this.prevSegmentPitch[index], this.segmentPitch[index]);
        float reference = index <= 1 ? bodyPitch : Mth.lerp(partialTick, this.prevSegmentPitch[index - 1], this.segmentPitch[index - 1]);
        return current - reference;
    }
}