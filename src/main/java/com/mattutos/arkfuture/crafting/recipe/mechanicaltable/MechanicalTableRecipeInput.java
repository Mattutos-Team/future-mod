package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MechanicalTableRecipeInput(ItemStack base,
                                         ItemStack pliers,
                                         List<ItemStack> inputs,
                                         Integer energyExpenditurePerTick,
                                         Integer maxTickPerCraft) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int pIndex) {
        return switch (pIndex) {
            case 0 -> this.base;
            case 1 -> this.pliers;
            case 2, 3, 4, 5 -> this.inputs.get(pIndex - 2);
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + pIndex);
        };
    }

    @Override
    public int size() {
        return 6;
    }

}
