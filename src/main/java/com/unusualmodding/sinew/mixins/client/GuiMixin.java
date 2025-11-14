package com.unusualmodding.sinew.mixins.client;

import com.unusualmodding.sinew.event.custom.RenderHeartEvent;
import com.unusualmodding.sinew.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "renderHeart", at = @At("RETURN"), cancellable = true)
    private void sinew$renderHeart(GuiGraphics graphics, Gui.HeartType type,
                                   int x, int y, int v, boolean blinking, boolean halfHeart,
                                   CallbackInfo ci) {

        Minecraft mc = ClientUtils.getMinecraft();

        if (!(mc.cameraEntity instanceof Player player)) return;

        RenderHeartEvent event = new RenderHeartEvent(
                graphics, player, type, x, y, v, blinking, halfHeart
        );

        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }


}
