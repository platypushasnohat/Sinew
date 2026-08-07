package com.platypushasnohat.sinew.api.entity.animation;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

public class SmoothAnimationState extends AnimationState {

    private static final float STOP_THRESHOLD = 0.001F;

    public float prevFactor;
    public float factor;
    public final float lerpSpeed;

    public SmoothAnimationState() {
        this(0.5F);
    }

    public SmoothAnimationState(float lerpSpeed) {
        this.lerpSpeed = lerpSpeed;
    }

    @Override
    public void animateWhen(boolean condition, int tickCount) {
        float target = condition ? 1.0F : 0.0F;
        this.prevFactor = this.factor;
        this.factor += (target - this.factor) * this.lerpSpeed;
        this.factor = Mth.clamp(this.factor, 0.0F, 1.0F);
        if (condition) {
            this.startIfStopped(tickCount);
        } else if (this.factor < STOP_THRESHOLD) {
            this.stop();
        }
    }

    public float factor(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevFactor, this.factor);
    }
}