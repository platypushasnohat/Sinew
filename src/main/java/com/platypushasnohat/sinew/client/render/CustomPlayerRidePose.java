package com.platypushasnohat.sinew.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface CustomPlayerRidePose {

    default void applyRiderPose(HumanoidModel<?> humanoidModel, LivingEntity rider) {
    }

    default void applyRiderPoseStack(Entity entity, PoseStack poseStack) {
    }

    default float toRadians(float degree) {
        return (float) Math.toRadians(degree);
    }
}