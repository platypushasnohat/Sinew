package com.platypushasnohat.sinew.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

public abstract class RiderLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public RiderLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    public static <E extends Entity> void renderPassenger(E entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entity);
            try {
                render.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
            } catch (Throwable throwable) {
                throw new ReportedException(CrashReport.forThrowable(throwable, "Rendering entity in world"));
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entity.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory details = crashreport.addCategory("Renderer details");
            if (render != null) {
                details.setDetail("Assigned renderer", render);
            }
            details.setDetail("Rotation", yaw);
            details.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }
}
