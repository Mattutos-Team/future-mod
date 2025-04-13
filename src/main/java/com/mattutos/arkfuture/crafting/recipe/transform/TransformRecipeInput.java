package com.mattutos.arkfuture.crafting.recipe.transform;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record TransformRecipeInput(List<ItemStack> items) implements RecipeInput {

    @Override
    public ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public int size() {
        return items().size();
    }
}
