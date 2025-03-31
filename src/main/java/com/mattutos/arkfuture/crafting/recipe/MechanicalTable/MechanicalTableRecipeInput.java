package com.mattutos.arkfuture.crafting.recipe.MechanicalTable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MechanicalTableRecipeInput implements RecipeInput {
    private static final Logger log = LoggerFactory.getLogger(MechanicalTableRecipeInput.class);
    private final List<ItemStack> inputs;

    public MechanicalTableRecipeInput(List<ItemStack> inputs) {
        this.inputs = inputs;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        if (pIndex >= 0 && pIndex < inputs.size()) {
            return inputs.get(pIndex);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return inputs.size();
    }
}
