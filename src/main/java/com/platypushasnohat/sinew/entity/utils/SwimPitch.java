package com.platypushasnohat.sinew.entity.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SwimPitch {

    public final Entity entity;

    public final float pitchClamp;
    public final float pitchEase;
    public final float rollClamp;
    public final float rollEase;

    public float prevRoll;
    public float roll;

    public float prevPitch;
    public float pitch;

    public SwimPitch(Entity entity, float pitchClamp, float rollClamp) {
        this(entity, pitchClamp, 0.2F, rollClamp, 0.85F);
    }

    public SwimPitch(Entity entity, float pitchClamp, float pitchEase, float rollClamp, float rollEase) {
        this.entity = entity;
        this.pitchClamp = pitchClamp;
        this.pitchEase = pitchEase;
        this.rollClamp = rollClamp;
        this.rollEase = rollEase;
    }

    public void tick() {
        this.updateRoll();
        this.updatePitch();
    }

    public void updateRoll() {
        this.prevRoll = this.roll;
        if (this.entity.isInWater()) {
            float turn = Mth.degreesDifference(this.entity.getYRot(), this.entity.yRotO);
            if (Math.abs(turn) > 1.0F) {
                if (Math.abs(this.roll) < this.rollClamp) {
                    this.roll -= Math.signum(turn);
                }
            } else if (this.roll != 0.0F) {
                float sign = Math.signum(this.roll);
                this.roll -= sign * this.rollEase;
                if (this.roll * sign < 0.0F) {
                    this.roll = 0.0F;
                }
            }
        } else {
            this.roll = 0.0F;
        }
    }

    public void updatePitch() {
        this.prevPitch = this.pitch;
        float target = 0.0F;
        if (this.entity.isInWater()) {
            double dx = this.entity.getX() - this.entity.xo;
            double dy = this.entity.getY() - this.entity.yo;
            double dz = this.entity.getZ() - this.entity.zo;
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            double speed = Math.sqrt(horizontal * horizontal + dy * dy);
            float speedFactor = (float) Mth.clamp((speed - 0.01D) / (0.05D - 0.01D), 0.0D, 1.0D);
            if (speedFactor > 0.0F) {
                float angle = (float) (-(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG));
                target = Mth.clamp(angle, -this.pitchClamp, this.pitchClamp) * speedFactor;
            }
        }
        this.pitch += (target - this.pitch) * this.pitchEase;
    }

    public float getRoll(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevRoll, this.roll);
    }

    public float getPitch(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevPitch, this.pitch);
    }
}