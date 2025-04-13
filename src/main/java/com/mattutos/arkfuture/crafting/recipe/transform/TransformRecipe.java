package com.mattutos.arkfuture.crafting.recipe.transform;

import com.mattutos.arkfuture.init.recipe.ModRecipe;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TransformRecipe(NonNullList<Ingredient> ingredients, ItemStack output,
                              TransformCircumstance circumstance) implements Recipe<TransformRecipeInput> {


    @Override
    public boolean matches(TransformRecipeInput pInput, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(TransformRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.TRANSFORM_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipe.TRANSFORM_RECIPE_TYPE.get();
    }
}
