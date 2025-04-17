package com.mattutos.arkfuture.crafting.recipe.explosion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ExplosionRecipeInput(List<ItemStack> ingredients) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        return ingredients.get(pIndex);
    }

    @Override
    public int size() {
        return ingredients.size();
    }
}
