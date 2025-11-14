package com.unusualmodding.sinew.event.custom;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class RenderHeartEvent extends net.minecraftforge.eventbus.api.Event {

    private final GuiGraphics graphics;
    private final Gui.HeartType heartType;
    private final int x, y, v;
    private final boolean blinking, halfHeart;
    private boolean canceled = false;
    private final Player player;
    public RenderHeartEvent(GuiGraphics graphics, Player player, Gui.HeartType heartType,
                            int x, int y, int v, boolean blinking, boolean halfHeart) {
        this.graphics = graphics;
        this.heartType = heartType;
        this.x = x;
        this.y = y;
        this.v = v;
        this.blinking = blinking;
        this.halfHeart = halfHeart;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public GuiGraphics getGraphics() { return graphics; }
    public Gui.HeartType getHeartType() { return heartType; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getV() { return v; }
    public boolean isBlinking() { return blinking; }
    public boolean isHalfHeart() { return halfHeart; }

    public void cancel() { this.canceled = true; }
    public boolean isCanceled() { return canceled; }
}
