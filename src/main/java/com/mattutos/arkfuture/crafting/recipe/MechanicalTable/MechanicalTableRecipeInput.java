package com.mattutos.arkfuture.crafting.recipe.MechanicalTable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class MechanicalTableRecipeInput implements RecipeInput {
    private final ItemStack[] inputs;  // Array to store the 5 input ItemStacks

    // Constructor now takes an array of ItemStacks
    public MechanicalTableRecipeInput(ItemStack... inputs) {
        if (inputs.length != 5) {
            throw new IllegalArgumentException("MechanicalTableRecipeInput must have exactly 5 inputs.");
        }
        this.inputs = inputs;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        if (pIndex < 0 || pIndex >= size()) {
            return ItemStack.EMPTY;  // Return empty stack if the index is out of bounds
        }
        return inputs[pIndex];  // Return the item at the specified index
    }

    @Override
    public int size() {
        return 5;  // We now have 5 input slots
    }

    // Optional: you can create a getter for the inputs array if you need it elsewhere
    public ItemStack[] getInputs() {
        return inputs;
    }
}
