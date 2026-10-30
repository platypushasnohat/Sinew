package com.platypushasnohat.sinew.client.utils;

import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class ClientItemData {

    private static final WeakHashMap<ItemStack, Map<String, Object>> dataMap = new WeakHashMap<>();

    public static Map<String, Object> getOrCreateClientData(ItemStack stack) {
        return dataMap.computeIfAbsent(stack, itemStack -> new HashMap<>());
    }

    public static Map<String, Object> getClientData(ItemStack stack) {
        return dataMap.get(stack);
    }

    public static boolean hasClientData(ItemStack stack, String data) {
        if (stack == null) {
            return false;
        }
        Map<String, Object> map = dataMap.get(stack);
        return map != null && map.containsKey(data);
    }

    public static void copyClientData(ItemStack from, ItemStack to) {
        Map<String, Object> fromData = dataMap.get(from);
        if (fromData != null) {
            dataMap.put(to, new HashMap<>(fromData));
        }
    }

    public static void removeClientData(ItemStack stack) {
        dataMap.remove(stack);
    }
}