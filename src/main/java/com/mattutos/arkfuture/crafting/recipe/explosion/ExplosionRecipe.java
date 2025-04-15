package com.mattutos.arkfuture.crafting.recipe.explosion;

import com.mattutos.arkfuture.init.recipe.ModRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ExplosionRecipe(List<ItemStack> ingredients,
                              ItemStack output) implements Recipe<ExplosionRecipeInput> {


    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingredients.stream().map(i -> Ingredient.of(i.getItem())).toList());
        return list;
    }

    @Override
    public boolean matches(ExplosionRecipeInput pInput, Level pLevel) {
        if (pLevel.isClientSide()) return false;

        boolean isIngredientMatching = false;

        for (Ingredient ingredient : getIngredients()) {
            isIngredientMatching = false;
            for (int i = 0; i < pInput.size(); i++) {
                ItemStack stack = pInput.getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    isIngredientMatching = true;
                    break;
                }
            }
            if (!isIngredientMatching) return false;
        }

        return isIngredientMatching;
    }

    @Override
    public ItemStack assemble(ExplosionRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.EXPLOSION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipe.EXPLOSION_RECIPE_TYPE.get();
    }
}
