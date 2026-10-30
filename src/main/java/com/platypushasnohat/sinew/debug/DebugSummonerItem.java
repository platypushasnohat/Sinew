package com.platypushasnohat.sinew.debug;

import com.platypushasnohat.sinew.entity.base.SummonableMonster;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class DebugSummonerItem extends Item {

    private final Supplier<? extends EntityType<? extends SummonableMonster>> summonType;
    private final boolean mount;

    public DebugSummonerItem(Supplier<? extends EntityType<? extends SummonableMonster>> summonType, boolean mount, Properties properties) {
        super(properties);
        this.summonType = summonType;
        this.mount = mount;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            Vec3 position = this.mount ? player.position() : player.getEyePosition().add(player.getLookAngle());
            SummonableMonster summon = SummonableMonster.summon(this.summonType.get(), serverLevel, player, stack, hand, position);
            if (summon == null) {
                player.displayClientMessage(Component.translatable("message.sinew.not_enough_minion_slots"), true);
            }
            else {
                if (!this.mount) {
                    summon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 1.0F);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        player.getCooldowns().addCooldown(this, 20);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
