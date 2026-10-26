package com.platypushasnohat.sinew.entity.elite;

import com.platypushasnohat.sinew.Sinew;
import com.platypushasnohat.sinew.config.SinewConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;

public interface EliteMob {

    ResourceLocation ELITE_BONUS_ID = Sinew.location("elite_bonus");

    boolean isElite();

    void setElite(boolean elite);

    default double getEliteChance() {
        return SinewConfig.ELITE_MOB_CHANCE.get();
    }

    default double getEliteMultiplier() {
        return 1.5D;
    }

    @Nullable
    default ResourceKey<LootTable> getEliteLootTable() {
        return null;
    }

    default void makeElite() {
        Mob mob = (Mob) this;
        this.setElite(true);
        this.applyEliteModifiers();
        mob.setHealth(mob.getMaxHealth());
    }

    default void loadElite(boolean elite) {
        Mob mob = (Mob) this;
        AttributeInstance health = mob.getAttribute(Attributes.MAX_HEALTH);
        boolean fresh = health != null && !health.hasModifier(ELITE_BONUS_ID);
        this.setElite(elite);
        if (elite) {
            this.applyEliteModifiers();
        }
        if (elite && fresh) {
            mob.setHealth(mob.getMaxHealth());
        }
    }

    default void applyEliteModifiers() {
        this.applyEliteModifier(Attributes.MAX_HEALTH);
        this.applyEliteModifier(Attributes.ARMOR);
        this.applyEliteModifier(Attributes.ATTACK_DAMAGE);
    }

    private void applyEliteModifier(Holder<Attribute> attribute) {
        AttributeInstance instance = ((Mob) this).getAttribute(attribute);
        if (instance != null) {
            instance.addOrReplacePermanentModifier(new AttributeModifier(ELITE_BONUS_ID, this.getEliteMultiplier() - 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }
}
