package com.unusualmodding.sinew.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.event.custom.RenderHeartEvent;
import com.unusualmodding.sinew.heart_type.HeartTypeRegistry;
import com.unusualmodding.sinew.heart_type.SinewHeartType;
import com.unusualmodding.sinew.managers.ScreenshakeManager;
import com.unusualmodding.sinew.utils.ClientUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sinew.MODID, value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderHeart(RenderHeartEvent event) {

        for (SinewHeartType t : HeartTypeRegistry.getTypes()) {
            if (!t.applies(event.getPlayer(), event.isBlinking()) || !t.drawForHeartType(event.getHeartType())) continue;
            Level level = event.getPlayer().level();
            if(level == null) return;
            boolean hardcore = level.getLevelData().isHardcore();
            Pair<Integer, Integer> uv = event.isHalfHeart()
                    ? t.getHalfHeartPos(hardcore)
                    : t.getHeartPos(hardcore);

            RenderSystem.setShaderTexture(0, t.getAtlas());
            event.getGraphics().blit(t.getAtlas(),
                    event.getX(), event.getY(),
                    uv.getFirst(), uv.getSecond(),
                    9, 9,
                    t.getAtlasWidth(),
                    t.getAtlasHeight());
            RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
            event.cancel();
            return;
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = ClientUtils.getMinecraft();
        final ClientLevel level = ClientUtils.getLevel();
        if (level != null) {
            if (minecraft.isPaused()) {
                return;
            }
            Camera camera = minecraft.gameRenderer.getMainCamera();

            ScreenshakeManager.clientTick(level, camera);

        }
    }

    @SubscribeEvent
    public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
        ScreenshakeManager.computeAngles(event);
    }
}
