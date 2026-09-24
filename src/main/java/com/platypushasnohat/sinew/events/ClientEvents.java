package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.render.CustomPlayerRidePose;
import com.platypushasnohat.sinew.events.custom.PlayerPoseEvent;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import com.platypushasnohat.sinew.utils.SinewClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SuppressWarnings("UnstableApiUsage")
    @SubscribeEvent
    public static void preRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        if (SinewClientProxy.blockedEntityRenders.contains(event.getEntity().getUUID())) {
            if (!Sinew.PROXY.isFirstPersonPlayer(event.getEntity())) {
                NeoForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
                event.setCanceled(true);
            }
            SinewClientProxy.blockedEntityRenders.remove(event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerPose(PlayerPoseEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && player.getVehicle() != null) {
            if (Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player.getVehicle()) instanceof CustomPlayerRidePose ridePose) {
                ridePose.applyRiderPose(event.getHumanoidModel(), entity);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();

        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SkinLayerHidingArmorItem) {
            model.hat.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SkinLayerHidingArmorItem) {
            model.jacket.visible = false;
            model.leftSleeve.visible = false;
            model.rightSleeve.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SkinLayerHidingArmorItem) {
            model.leftPants.visible = false;
            model.rightPants.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SkinLayerHidingArmorItem) {
            model.leftPants.visible = false;
            model.rightPants.visible = false;
        }
    }
}
