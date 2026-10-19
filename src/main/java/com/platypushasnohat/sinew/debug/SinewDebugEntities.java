package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.Sinew;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//DEBUG
//summon with /summon sinew:spectral_test_zombie or /summon sinew:spectral_test_visible_zombie
public class SinewDebugEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Sinew.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SpectralTestZombie>> SPECTRAL_TEST_ZOMBIE = ENTITY_TYPES.register("spectral_test_zombie", () -> EntityType.Builder.of(SpectralTestZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("spectral_test_zombie"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralTestVisibleZombie>> SPECTRAL_TEST_VISIBLE_ZOMBIE = ENTITY_TYPES.register("spectral_test_visible_zombie", () -> EntityType.Builder.of(SpectralTestVisibleZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("spectral_test_visible_zombie"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralBlindWolf>> SPECTRAL_BLIND_WOLF = ENTITY_TYPES.register("spectral_blind_wolf", () -> EntityType.Builder.of(SpectralBlindWolf::new, MobCategory.CREATURE).sized(0.6F, 0.85F).build("spectral_blind_wolf"));

    @EventBusSubscriber(modid = Sinew.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {

        @SubscribeEvent
        public static void onAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(SPECTRAL_TEST_ZOMBIE.get(), Zombie.createAttributes().build());
            event.put(SPECTRAL_TEST_VISIBLE_ZOMBIE.get(), Zombie.createAttributes().build());
            event.put(SPECTRAL_BLIND_WOLF.get(), Wolf.createAttributes().build());
        }
    }

    @EventBusSubscriber(modid = Sinew.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(SPECTRAL_TEST_ZOMBIE.get(), ZombieRenderer::new);
            event.registerEntityRenderer(SPECTRAL_TEST_VISIBLE_ZOMBIE.get(), ZombieRenderer::new);
            event.registerEntityRenderer(SPECTRAL_BLIND_WOLF.get(), WolfRenderer::new);
        }
    }
}
