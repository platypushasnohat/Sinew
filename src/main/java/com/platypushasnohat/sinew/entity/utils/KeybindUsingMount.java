package com.platypushasnohat.sinew.entity.utils;

import net.minecraft.world.entity.Entity;

public interface KeybindUsingMount {

    void onKeyPacket(Entity keyPresser, int type);
}