package com.platypushasnohat.sinew.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class AgeableSinewEntityModel<E extends Entity> extends SinewEntityModel<E> {

    private final float youngScaleFactor;
    private final float bodyYOffset;

    public AgeableSinewEntityModel(float youngScaleFactor, float bodyYOffset) {
        this(RenderType::entityCutoutNoCull, youngScaleFactor, bodyYOffset);
    }

    public AgeableSinewEntityModel(Function<ResourceLocation, RenderType> renderType, float youngScaleFactor, float bodyYOffset) {
        super(renderType);
        this.bodyYOffset = bodyYOffset;
        this.youngScaleFactor = youngScaleFactor;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.young) {
            poseStack.pushPose();
            poseStack.scale(this.youngScaleFactor, this.youngScaleFactor, this.youngScaleFactor);
            poseStack.translate(0.0F, this.bodyYOffset / 16.0F, 0.0F);
            this.root().render(poseStack, buffer, packedLight, packedOverlay, color);
            poseStack.popPose();
        } else {
            this.root().render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
