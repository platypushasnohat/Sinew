package com.platypushasnohat.sinew.item;

import com.platypushasnohat.sinew.registry.SinewDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

//game time each animation started on this stack, makes items individual instead of sharing one state
//data is not saved
//must sync server to client
//Item SHOULD override shouldCauseReequipAnimation to prevent the weird equip anim from replaying
public record ItemAnimations(Map<String, Long> startTimes) {

    public static final StreamCodec<ByteBuf, ItemAnimations> STREAM_CODEC = ByteBufCodecs.<ByteBuf, String, Long, Map<String, Long>>map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.VAR_LONG).map(ItemAnimations::new, ItemAnimations::startTimes);

    public ItemAnimations {
        startTimes = Map.copyOf(startTimes);
    }

    public static void start(LivingEntity holder, ItemStack stack, String name) {
        Map<String, Long> startTimes = new HashMap<>(getStartTimes(stack));
        startTimes.put(name, getStartTime(holder));
        stack.set(SinewDataComponents.ITEM_ANIMATIONS.get(), new ItemAnimations(startTimes));
    }

    public static void startIfStopped(LivingEntity holder, ItemStack stack, String name) {
        if (!isStarted(stack, name)) {
            start(holder, stack, name);
        }
    }

    public static void stop(ItemStack stack, String name) {
        if (isStarted(stack, name)) {
            Map<String, Long> startTimes = new HashMap<>(getStartTimes(stack));
            startTimes.remove(name);
            if (startTimes.isEmpty()) {
                stack.remove(SinewDataComponents.ITEM_ANIMATIONS.get());
            }
            else {
                stack.set(SinewDataComponents.ITEM_ANIMATIONS.get(), new ItemAnimations(startTimes));
            }
        }
    }

    public static void animateWhen(boolean condition, LivingEntity holder, ItemStack stack, String name) {
        if (condition) {
            startIfStopped(holder, stack, name);
        }
        else {
            stop(stack, name);
        }
    }

    public static boolean isStarted(ItemStack stack, String name) {
        return getStartTimes(stack).containsKey(name);
    }

    public static Map<String, Long> getStartTimes(ItemStack stack) {
        ItemAnimations animations = stack.get(SinewDataComponents.ITEM_ANIMATIONS.get());
        return animations == null ? Map.of() : animations.startTimes();
    }

    private static long getStartTime(LivingEntity holder) {
        long time = holder.level().getGameTime();
        if (holder instanceof ServerPlayer player) {
            time -= player.connection.latency() / 50;
        }
        return time;
    }
}
