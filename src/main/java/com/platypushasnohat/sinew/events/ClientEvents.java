package com.platypushasnohat.sinew.events;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.client.render.CustomPlayerRidePose;
import com.platypushasnohat.sinew.entity.base.SummonableMount;
import com.platypushasnohat.sinew.events.custom.PlayerPoseEvent;
import com.platypushasnohat.sinew.events.custom.ScreenShakeEvent;
import com.platypushasnohat.sinew.item.SkinLayerHidingArmorItem;
import com.platypushasnohat.sinew.network.MountedEntityKeyPacket;
import com.platypushasnohat.sinew.utils.SinewClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = Sinew.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static float shakeAmount;
    private static float prevShakeAmount;

    public static final List<ScreenShakeEvent> SCREEN_SHAKE_EVENTS = new ArrayList<>();

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

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.isAttack() && minecraft.player != null && minecraft.player.getVehicle() instanceof SummonableMount mount && mount.getControllingPassenger() == minecraft.player && mount.acceptsRiderKey(SummonableMount.RIDER_ATTACK)) {
            PacketDistributor.sendToServer(new MountedEntityKeyPacket(mount.getId(), minecraft.player.getId(), SummonableMount.RIDER_ATTACK));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();
        prevShakeAmount = shakeAmount;
        float shake = 0.0F;
        Iterator<ScreenShakeEvent> groundShakeMomentIterator = SCREEN_SHAKE_EVENTS.iterator();
        while (groundShakeMomentIterator.hasNext()) {
            ScreenShakeEvent groundShakeMoment = groundShakeMomentIterator.next();
            groundShakeMoment.tick();
            if (groundShakeMoment.isDone()) {
                groundShakeMomentIterator.remove();
            }
            else if (cameraEntity != null) {
                shake = Math.max(shake, groundShakeMoment.getDegree(cameraEntity, 1.0F));
            }
        }
        shakeAmount = shake * minecraft.options.screenEffectScale().get().floatValue();
    }

    @SubscribeEvent
    public void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);;
        float lerpedShakeAmount = Mth.clamp(prevShakeAmount + (shakeAmount - prevShakeAmount) * partialTicks, 0, 4.0F);
        if (lerpedShakeAmount > 0) {
            float time = minecraft.cameraEntity == null ? 0.0F : minecraft.cameraEntity.tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            event.setRoll((float) (lerpedShakeAmount * Math.sin(2.0F * time)));
        }
    }
}
