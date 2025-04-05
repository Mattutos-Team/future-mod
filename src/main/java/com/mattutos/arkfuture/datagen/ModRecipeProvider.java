package com.mattutos.arkfuture.datagen;

import com.mattutos.arkfuture.ArkFuture;

import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ANCIENT_ORE_BLOCK_ITEM.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ItemInit.ANCIENT_ORE_ITEM.get())
                .unlockedBy(getHasName(ItemInit.ANCIENT_ORE_ITEM.get()), has(ItemInit.ANCIENT_ORE_ITEM.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ItemInit.ANCIENT_ORE_INGOT_ITEM.get())
                .unlockedBy(getHasName(ItemInit.ANCIENT_ORE_INGOT_ITEM.get()), has(ItemInit.ANCIENT_ORE_INGOT_ITEM.get()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.ANCIENT_ORE_ITEM.get(), 9)
                .requires(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get())
                .unlockedBy(getHasName(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get()), has(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.ANCIENT_ORE_INGOT_ITEM.get(), 9)
                .requires(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get())
                .unlockedBy(getHasName(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get()), has(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get()))
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
            String cookedItemPid =
                    ArkFuture.MOD_ID + ":" + RecipeProvider.getItemName(pResult) + pRecipeName + "_" + RecipeProvider.getItemName(itemlike);

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
