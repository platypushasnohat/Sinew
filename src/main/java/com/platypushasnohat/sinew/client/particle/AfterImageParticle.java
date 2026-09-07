package com.platypushasnohat.sinew.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AfterImageParticle extends Particle {

    public final LivingEntity entity;
    public int life;

    public AfterImageParticle(LivingEntity entity, ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.entity = entity;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPosition = camera.getPosition();
        EntityRenderDispatcher dispatcher  = Minecraft.getInstance().getEntityRenderDispatcher();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.3F / Math.abs((float) this.life + 1));
        PoseStack poseStack = new PoseStack();
        if (dispatcher.getRenderer(this.entity) instanceof LivingEntityRenderer renderer) {
            poseStack.pushPose();
            bufferSource.getBuffer(RenderType.entityTranslucent(dispatcher.getRenderer(this.entity).getTextureLocation(this.entity)));
            EntityModel model = renderer.getModel();
            model.attackTime = this.entity.getAttackAnim(partialTicks);
            model.young = this.entity.isBaby();
            float yBodyRot = Mth.rotLerp(partialTicks, this.entity.yBodyRotO, this.entity.yBodyRot);
            float yHeadRot = Mth.rotLerp(partialTicks, this.entity.yHeadRotO, this.entity.yHeadRot);
            float yDiff = yHeadRot - yBodyRot;
            poseStack.translate(this.x - cameraPosition.x, (this.y - cameraPosition.y) + 1.4F, this.z - cameraPosition.z);
            float xRot = Mth.lerp(partialTicks, this.entity.xRotO, this.entity.getXRot());
            float ageInTicks = (float) this.entity.tickCount + partialTicks;
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            float walkSpeed = 0.0F;
            float walkPosition = 0.0F;
            if (this.entity.isAlive()) {
                walkSpeed = this.entity.walkAnimation.speed(partialTicks);
                walkPosition = this.entity.walkAnimation.position(partialTicks);
                if (this.entity.isBaby()) {
                    walkPosition *= 3.0F;
                }
                if (walkSpeed > 1.0F) {
                    walkSpeed = 1.0F;
                }
            }
            model.prepareMobModel(this.entity, walkPosition, walkSpeed, partialTicks);
            model.setupAnim(this.entity, walkPosition, walkSpeed, ageInTicks, yDiff, xRot);
            ResourceLocation textureLocation = renderer.getTextureLocation(this.entity);
            RenderType renderType = RenderType.entityTranslucent(textureLocation);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            if (!this.entity.isInvisible()) {
                model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, LivingEntityRenderer.getOverlayCoords(this.entity, 0.0F), -1);
            }
            poseStack.popPose();
        }
        bufferSource.endBatch();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void tick() {
        this.life++;
        if (this.life >= 3) {
            this.remove();
        }
    }
}