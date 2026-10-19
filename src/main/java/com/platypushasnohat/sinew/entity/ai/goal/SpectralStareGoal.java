package com.platypushasnohat.sinew.entity.ai.goal;

import com.platypushasnohat.sinew.entity.spectral.SpectralEntity;
import com.platypushasnohat.sinew.entity.spectral.SpectralSight;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

//this.targetSelector.addGoal(1, new SpectralStareGoal(this));
//used by: every spectral mob that turns hostile when watched. not the Grell (Sulcus) it has a custom
public class SpectralStareGoal extends Goal {

    public final Mob mob;
    public final int stareTicks;
    public final double range;
    //canUse only runs every other tick
    protected final Object2IntMap<UUID> stareStarts = new Object2IntOpenHashMap<>();
    protected Player starer;

    public <T extends Mob & SpectralEntity> SpectralStareGoal(T mob) {
        this(mob, mob.getStareTicksToTarget(), 32.0D);
    }

    public SpectralStareGoal(Mob mob, int stareTicks, double range) {
        this.mob = mob;
        this.stareTicks = stareTicks;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
            this.stareStarts.clear();
            return false;
        }
        List<Player> starers = this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.range), this::isStaring);
        this.stareStarts.keySet().removeIf(uuid -> starers.stream().noneMatch(player -> player.getUUID().equals(uuid)));
        this.starer = null;
        for (Player player : starers) {
            if (!this.stareStarts.containsKey(player.getUUID())) {
                this.stareStarts.put(player.getUUID(), this.mob.tickCount);
            }
            if (this.mob.tickCount - this.stareStarts.getInt(player.getUUID()) >= this.stareTicks && this.starer == null) {
                this.starer = player;
            }
        }
        return this.starer != null;
    }

    protected boolean isStaring(Player player) {
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && SpectralSight.canPerceive(player, this.mob) && SpectralSight.isLookingAt(player, this.mob);
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.starer);
        this.stareStarts.clear();
        this.starer = null;
    }
}
