package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public class MechanicalTableRecipeInput implements RecipeInput {
    private final List<ItemStack> inputs;
    private final ItemStack baseItem;

    public MechanicalTableRecipeInput(List<ItemStack> inputs, ItemStack baseItem) {
        this.inputs = inputs;
        this.baseItem = baseItem;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        // Base item is always at the last index
        if (pIndex >= 0 && pIndex < inputs.size()) {
            return inputs.get(pIndex);
        } else if (pIndex == inputs.size()) {
            return baseItem;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return inputs.size() + 1;  // Account for base item
    }
}
