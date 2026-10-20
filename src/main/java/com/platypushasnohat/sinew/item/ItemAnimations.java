package com.platypushasnohat.sinew.item;

import com.platypushasnohat.sinew.registry.SinewDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

//game time each animation started on this stack, makes items individual instead of sharing one state
//data is not saved
//must sync server to client
//Item SHOULD override shouldCauseReequipAnimation to prevent the weird equip anim from replaying
public record ItemAnimations(Map<String, Long> startTimes) {

    //client only, when the local player last started each animation, per item and animation name
    private static final Map<String, Long> PREDICTED_START_TIMES = new HashMap<>();
    //how many ticks apart the server's start time and the client's can be and still count as the same start
    private static final int PREDICTION_WINDOW = 5;

    public static final StreamCodec<ByteBuf, ItemAnimations> STREAM_CODEC = ByteBufCodecs.<ByteBuf, String, Long, Map<String, Long>>map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.VAR_LONG).map(ItemAnimations::new, ItemAnimations::startTimes);

    public ItemAnimations {
        startTimes = Map.copyOf(startTimes);
    }

    public static void start(LivingEntity holder, ItemStack stack, String name) {
        long time = holder.level().getGameTime();
        if (holder.level().isClientSide) {
            PREDICTED_START_TIMES.put(getPredictionKey(stack, name), time);
        }
        Map<String, Long> startTimes = new HashMap<>(getStartTimes(stack));
        startTimes.put(name, time);
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

    //the start time a model should play
    //server copy of a start the player already predicted is ticks off and plays animation twice. With this client time is kept
    public static Long getDisplayedStartTime(ItemStack stack, String name) {
        Long startTime = getStartTimes(stack).get(name);
        Long predicted = PREDICTED_START_TIMES.get(getPredictionKey(stack, name));
        if (startTime != null && predicted != null && Math.abs(startTime - predicted) <= PREDICTION_WINDOW) {
            startTime = predicted;
        }
        return startTime;
    }

    private static String getPredictionKey(ItemStack stack, String name) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()) + "/" + name;
    }
}