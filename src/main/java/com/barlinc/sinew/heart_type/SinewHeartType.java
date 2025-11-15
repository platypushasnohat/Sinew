package com.barlinc.sinew.heart_type;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface SinewHeartType {
    static final int ATLAS_W = 64;
    static final int ATLAS_H = 9;
    static final int SIZE = 9;
    static final int verticalIndex = 0;
    ResourceLocation getAtlas();
    default Pair<Integer, Integer> getHeartPos(boolean hardcore) {
        int y = verticalIndex * SIZE;
        int x = (hardcore ? 2 : 0) * SIZE;
        return Pair.of(x, y);
    }
    default Pair<Integer, Integer> getHalfHeartPos(boolean hardcore) {
        int y = verticalIndex * SIZE;
        int x = (hardcore ? 3 : 1) * SIZE;
        return Pair.of(x, y);
    }
    default int getAtlasWidth() {
        return ATLAS_W;
    }
    default int getAtlasHeight() {
        return ATLAS_H;
    }
    boolean drawForHeartType(Gui.HeartType type);
    boolean applies(Player player, boolean blinking);
}
