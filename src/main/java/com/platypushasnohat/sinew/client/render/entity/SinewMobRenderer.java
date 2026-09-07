package com.platypushasnohat.sinew.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.sinew.entity.utils.BodyChainMob;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;

public abstract class SinewMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M> {

    public SinewMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected void setupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTicks, float scale) {
        if (entity instanceof BodyChainMob bodyChainMob) {
            super.setupRotations(entity, poseStack, bob, bodyChainMob.getRenderYaw(partialTicks), partialTicks, scale);
        }
        else {
            super.setupRotations(entity, poseStack, bob, yBodyRot, partialTicks, scale);
        }
    }
}
