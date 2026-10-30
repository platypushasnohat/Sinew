package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.registry.SinewAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SinewDebugItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Sinew.MOD_ID);

    private static final ResourceLocation SUMMONER_MODIFIER = Sinew.location("debug_summoner");

    public static final DeferredItem<DebugSummonerItem> DEBUG_SUMMONER = ITEMS.register("debug_summoner", () -> new DebugSummonerItem(SinewDebugEntities.SUMMON_TEST_ZOMBIE, false, summonerProperties(6.0D, 10.0D)));
    public static final DeferredItem<DebugSummonerItem> DEBUG_MOUNT_SUMMONER = ITEMS.register("debug_mount_summoner", () -> new DebugSummonerItem(SinewDebugEntities.SUMMON_TEST_MOUNT, true, summonerProperties(8.0D, 30.0D)));

    private static Item.Properties summonerProperties(double damage, double duration) {
        return new Item.Properties().stacksTo(1).attributes(ItemAttributeModifiers.builder()
                .add(SinewAttributes.MINION_DAMAGE, new AttributeModifier(SUMMONER_MODIFIER, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(SinewAttributes.MINION_DURATION, new AttributeModifier(SUMMONER_MODIFIER, duration, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build());
    }

    @EventBusSubscriber(modid = Sinew.MOD_ID)
    public static class CommonModEvents {

        @SubscribeEvent
        public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
                event.accept(DEBUG_SUMMONER);
                event.accept(DEBUG_MOUNT_SUMMONER);
            }
        }
    }
}
