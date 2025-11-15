package com.barlinc.sinew.datagen;

import com.barlinc.sinew.Sinew;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public abstract class SinewRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public final String modid;

    public SinewRecipeProvider(String modid, PackOutput output) {
        super(output);
        this.modid = modid;
    }

    public static void conditionalRecipe(RecipeBuilder recipe, ICondition condition, Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath())).build(consumer, id);
    }

    public static void woodSet(TagKey<Item> logs, Block planks, Block slab, Block stairs, Block log, Block wood, Block strippedLog, Block strippedWood, ItemLike boat, ItemLike chestBoat, Block button, Block door, Block trapdoor, Block fence, Block fenceGate, Block pressurePlate, Block sign, Block hangingSign, Consumer<FinishedRecipe> consumer) {
        woodenBoat(consumer, boat, planks);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat).group("chest_boat").requires(Tags.Items.CHESTS_WOODEN).requires(boat).unlockedBy(getHasName(boat), has(boat)).save(consumer, getSaveLocation(chestBoat));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button).group("wooden_button").requires(planks).unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(button));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, door, 3).group("wooden_door").define('#', planks).pattern("##").pattern("##").pattern("##").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(door));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3).group("wooden_fence").define('#', planks).define('S', Items.STICK).pattern("#S#").pattern("#S#").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(fence));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fenceGate).group("wooden_fence_gate").define('#', planks).define('S', Items.STICK).pattern("S#S").pattern("S#S").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(fenceGate));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressurePlate).group("wooden_pressure_plate").define('#', planks).pattern("##").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(pressurePlate));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3).group("wooden_sign").define('#', planks).define('S', Items.STICK).pattern("###").pattern("###").pattern(" S ").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(sign));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, trapdoor, 2).group("wooden_trapdoor").define('#', planks).pattern("###").pattern("###").unlockedBy(getHasName(planks), has(planks)).save(consumer, getSaveLocation(trapdoor));
        hangingSign(consumer, hangingSign, strippedLog);
        planksFromLogs(consumer, planks, logs, 4);
        woodFromLogs(consumer, wood, log);
        woodFromLogs(consumer, strippedWood, strippedLog);
        slab(planks, slab, "wooden_slab", consumer);
        stairs(planks, stairs, "wooden_stairs", consumer);
    }

    public static void stairs(ItemLike ingredient, ItemLike stairs, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4).define('#', ingredient).pattern("#  ").pattern("## ").pattern("###").unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(stairs)));
    }

    public static void stairs(ItemLike ingredient, ItemLike stairs, String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4).group(group).define('#', ingredient).pattern("#  ").pattern("## ").pattern("###").unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(stairs)));
    }

    public static void wall(ItemLike ingredient, ItemLike wall, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wall, 6).define('#', ingredient).pattern("###").pattern("###").unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(wall)));
    }

    public static void slab(ItemLike ingredient, ItemLike slab, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6).define('#', ingredient).pattern("###").unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(slab)));
    }

    public static void slab(ItemLike ingredient, ItemLike slab, String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6).group(group).define('#', ingredient).pattern("###").unlockedBy("has_" + getName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(slab)));
    }

    public ShapedRecipeBuilder door(Supplier<? extends Block> doorOut, Supplier<? extends Block> plankIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, doorOut.get(), 3)
                .pattern("PP")
                .pattern("PP")
                .pattern("PP")
                .define('P', plankIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(plankIn.get()).getPath(), has(plankIn.get()));
    }

    public ShapedRecipeBuilder trapdoor(Supplier<? extends Block> trapdoorOut, Supplier<? extends Block> plankIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, trapdoorOut.get(), 2)
                .pattern("PPP")
                .pattern("PPP")
                .define('P', plankIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(plankIn.get()).getPath(), has(plankIn.get()));
    }

    public ShapelessRecipeBuilder button(Supplier<? extends Block> buttonOut, Supplier<? extends Block> blockIn) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, buttonOut.get())
                .requires(blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder pressurePlate(Supplier<? extends Block> pressurePlateOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pressurePlateOut.get())
                .pattern("BB")
                .define('B', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder stairs(Supplier<? extends Block> stairsOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsOut.get(), 4)
                .pattern("M  ")
                .pattern("MM ")
                .pattern("MMM")
                .define('M', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder slab(Supplier<? extends Block> slabOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabOut.get(), 6)
                .pattern("MMM")
                .define('M', blockIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder fence(Supplier<? extends Block> fenceOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fenceOut.get(), 3)
                .pattern("M/M")
                .pattern("M/M")
                .define('M', blockIn.get())
                .define('/', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapedRecipeBuilder fenceGate(Supplier<? extends Block> fenceGateOut, Supplier<? extends Block> blockIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fenceGateOut.get())
                .pattern("/M/")
                .pattern("/M/")
                .define('M', blockIn.get())
                .define('/', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn.get()).getPath(), has(blockIn.get()));
    }

    public ShapelessRecipeBuilder planks(Supplier<? extends Block> plankOut, TagKey<Item> logIn) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, plankOut.get(), 4)
                .requires(logIn)
                .group("planks")
                .unlockedBy("has_log", has(logIn));
    }

    public ShapedRecipeBuilder wood(Supplier<? extends Block> woodOut, Supplier<? extends Block> logIn) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodOut.get(), 3)
                .pattern("MM")
                .pattern("MM")
                .define('M', logIn.get())
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(logIn.get()).getPath(), has(logIn.get()));
    }

    private static String getName(ItemLike object) {
        return ForgeRegistries.ITEMS.getKey(object.asItem()).getPath();
    }

    private static ResourceLocation getSaveLocation(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem());
    }

    public static ResourceLocation getSaveLocation(String name) {
        return new ResourceLocation(Sinew.MOD_ID, name);
    }
}
