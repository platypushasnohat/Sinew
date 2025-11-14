package com.unusualmodding.sinew.event;

import com.unusualmodding.sinew.Sinew;
import com.unusualmodding.sinew.mob_effects.SinewMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sinew.MODID ,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent
    public static void preventEating(LivingEntityUseItemEvent.Start event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.getItem();
        int duration = event.getDuration();
        if(stack.isEdible() && entity.hasEffect(SinewMobEffects.STENCH.get())) {
            event.setCanceled(true);
        }
    }
}
