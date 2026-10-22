package com.platypushasnohat.sinew.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.platypushasnohat.sinew.registry.SinewLootModifierTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ReplaceTableLootModifier extends LootModifier {

    public static final MapCodec<ReplaceTableLootModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(modifier -> modifier.conditions),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(modifier -> modifier.table)).apply(instance, ReplaceTableLootModifier::new));

    private final ResourceKey<LootTable> table;

    public ReplaceTableLootModifier(LootItemCondition[] conditions, ResourceKey<LootTable> table) {
        super(conditions);
        this.table = table;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        context.getResolver().get(Registries.LOOT_TABLE, this.table).ifPresent(replacement -> {
            generatedLoot.clear();
            replacement.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return SinewLootModifierTypes.REPLACE_TABLE.get();
    }
}
