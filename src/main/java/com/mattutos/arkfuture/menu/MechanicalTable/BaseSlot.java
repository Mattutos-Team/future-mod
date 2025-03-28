package com.mattutos.arkfuture.menu.MechanicalTable;

import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BaseSlot extends SlotItemHandler {

    private final MechanicalTableRecipe mechanicalTableRecipeRecipe;

    public BaseSlot(IItemHandler pItemHandler, int pSlot, int pX, int pY, MechanicalTableRecipe mechanicalTableRecipeRecipe) {
        super(pItemHandler, pSlot, pX, pY);
        this.mechanicalTableRecipeRecipe = mechanicalTableRecipeRecipe;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return isValid(pStack);
    }


    public boolean isValid(ItemStack itemStack) {

        if (itemStack.getItem().equals(mechanicalTableRecipeRecipe.base)) {
            return true;
        } else {
            return false;
        }

    }
}
