package com.platypushasnohat.sinew.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class SwimmingMoveControl extends MoveControl {

    public final int maxTurnX;
    public final int maxTurnY;
    public final float inWaterSpeedModifier;
    public final boolean headFollows;

    public SwimmingMoveControl(Mob mob, int maxTurnX, int maxTurnY, float inWaterSpeedModifier) {
        this(mob, maxTurnX, maxTurnY, inWaterSpeedModifier, true);
    }

    public SwimmingMoveControl(Mob mob, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, boolean headFollows) {
        super(mob);
        this.maxTurnX = maxTurnX;
        this.maxTurnY = maxTurnY;
        this.inWaterSpeedModifier = inWaterSpeedModifier;
        this.headFollows = headFollows;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double dx = this.wantedX - this.mob.getX();
            double dy = this.wantedY - this.mob.getY();
            double dz = this.wantedZ - this.mob.getZ();
            if (dx * dx + dy * dy + dz * dz < 2.5000003E-7D) {
                this.mob.setZza(0.0F);
            }
            else {
                float yRot = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, (float) this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                if (this.headFollows) {
                    this.mob.yHeadRot = this.mob.getYRot();
                }
                float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed(speed * this.inWaterSpeedModifier);
                    double horizontal = Math.sqrt(dx * dx + dz * dz);
                    float pitch = 0.0F;
                    if (Math.abs(dy) > 1.0E-5D || Math.abs(horizontal) > 1.0E-5D) {
                        pitch = (float) -(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG);
                        pitch = Mth.clamp(Mth.wrapDegrees(pitch), -this.maxTurnX, this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), pitch, 5.0F));
                    }
                    this.mob.zza = Mth.cos(pitch * Mth.DEG_TO_RAD) * speed;
                    this.mob.yya = -Mth.sin(pitch * Mth.DEG_TO_RAD) * speed;
                } else {
                    float rot = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - yRot));
                    float turningSpeed = getTurningSpeedFactor(rot);
                    this.mob.setSpeed(speed * 0.1F * turningSpeed);
                }
            }
        } else {
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }

    public static float getTurningSpeedFactor(float degreesToTurn) {
        return 1.0F - Mth.clamp((degreesToTurn - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}