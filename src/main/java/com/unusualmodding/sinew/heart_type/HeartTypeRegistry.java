package com.unusualmodding.sinew.heart_type;

import java.util.ArrayList;
import java.util.List;

public class HeartTypeRegistry {
    private static final List<SinewHeartType> TYPES = new ArrayList<>();

    public static void register(SinewHeartType type) {
        TYPES.add(type);
    }

    public static List<SinewHeartType> getTypes() {
        return TYPES;
    }
}
