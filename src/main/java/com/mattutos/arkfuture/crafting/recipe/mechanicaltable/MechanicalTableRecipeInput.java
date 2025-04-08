package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraftforge.items.IItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MechanicalTableRecipeInput implements RecipeInput {

    private static final Logger log = LoggerFactory.getLogger(MechanicalTableRecipeInput.class);
    // Fixed index mapping:
    // 0: Ingredient Slot 0
    // 1: Ingredient Slot 2
    // 2: Ingredient Slot 3
    // 3: Ingredient Slot 4
    // 4: Base Item (Slot 1)
    // 5: Mechanical Pliers (Slot 6)
    private final ItemStack[] inputs;

    public MechanicalTableRecipeInput(IItemHandler itemHandler) {
        this.inputs = new ItemStack[6];

        // A NEW ARRAY SEQUENCE TO MAP THE CORRECT SLOTS
        inputs[0] = itemHandler.getStackInSlot(0); // Ingredient 0
        inputs[1] = itemHandler.getStackInSlot(2); // Ingredient 1
        inputs[2] = itemHandler.getStackInSlot(3); // Ingredient 2
        inputs[3] = itemHandler.getStackInSlot(4); // Ingredient 3
        inputs[4] = itemHandler.getStackInSlot(1); // Base item
        inputs[5] = itemHandler.getStackInSlot(6); // Mechanical Pliers
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return (pIndex >= 0 && pIndex < inputs.length) ? inputs[pIndex] : ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return inputs.length;
    }
}
