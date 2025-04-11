package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;

import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        craft3x3(pRecipeOutput, ItemInit.ANCIENT_ORE_ITEM.get(), BlockInit.ANCIENT_ORE_BLOCK_ITEM.get(), 1);
        craft3x3(pRecipeOutput, ItemInit.ANCIENT_ORE_INGOT_ITEM.get(), BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get(), 1);

        craftConvert(pRecipeOutput, BlockInit.ANCIENT_ORE_BLOCK_ITEM.get(), ItemInit.ANCIENT_ORE_ITEM.get(), 9);
        craftConvert(pRecipeOutput, BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get(), ItemInit.ANCIENT_ORE_INGOT_ITEM.get(), 9);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.MECHANICAL_PLIERS.get())
                .pattern(" A ")
                .pattern("N N")
                .pattern("S S")
                .define('A', ItemInit.ANCIENT_ORE_INGOT_ITEM.get())
                .define('N', Items.IRON_NUGGET)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ItemInit.MECHANICAL_PLIERS.get()), has(ItemInit.MECHANICAL_PLIERS.get()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.GOLDEN_THREAD.get(), 24)
                .requires(Items.GOLD_INGOT, 1)
                .requires(ItemInit.MECHANICAL_PLIERS.get())
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(ItemInit.MECHANICAL_PLIERS.get()))
                .save(pRecipeOutput);
    }

    protected static void craft3x3(RecipeOutput pRecipeOutput, ItemLike pIngredient, ItemLike pResult, int pCount) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, pCount)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', pIngredient)
                .unlockedBy(getHasName(pIngredient), has(pIngredient))
                .save(pRecipeOutput);
    }

    protected static void craftConvert(RecipeOutput pRecipeOutput, ItemLike pIngredient, ItemLike pResult, int pCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, pCount)
                .requires(pIngredient)
                .unlockedBy(getHasName(pIngredient), has(pIngredient))
                .save(pRecipeOutput);
    }

    protected static void oreSmelting(
            RecipeOutput recipeOutput,
            List<ItemLike> pIngredients,
            RecipeCategory pCategory,
            ItemLike pResult,
            float pExperience,
            int pCookingTIme,
            String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(
            RecipeOutput recipeOutput,
            List<ItemLike> pIngredients,
            RecipeCategory pCategory,
            ItemLike pResult,
            float pExperience,
            int pCookingTime,
            String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(
            RecipeOutput recipeOutput,
            RecipeSerializer<T> pCookingSerializer,
            AbstractCookingRecipe.Factory<T> factory,
            List<ItemLike> pIngredients,
            RecipeCategory pCategory,
            ItemLike pResult,
            float pExperience,
            int pCookingTime,
            String pGroup,
            String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            String cookedItemPid = ArkFuture.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike);

            SimpleCookingRecipeBuilder.generic(
                            Ingredient.of(itemlike),
                            pCategory,
                            pResult,
                            pExperience,
                            pCookingTime,
                            pCookingSerializer,
                            factory)
                    .group(pGroup)
                    .unlockedBy(RecipeProvider.getHasName(itemlike), RecipeProvider.has(itemlike))
                    .save(recipeOutput, cookedItemPid);
        }
    }
}
