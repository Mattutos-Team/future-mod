package com.mattutos.arkfuture.menu.MechanicalTable;

import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipe;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;


//THIS BASE SLOT MUST BE USED WHEN YOUR CUSTOM MANU HAS ONLY ONE BASE ITEM TO CRAFT OTHER ITEMS

@Slf4j
public class BaseSlot extends SlotItemHandler {

    //TODO - ALLOW TO GET ANY RECIPE
    private final MechanicalTableRecipe mechanicalTableRecipe;

    public BaseSlot(IItemHandler pContainer, int pSlot, int pX, int pY, MechanicalTableRecipe mechanicalTableRecipe) {
        super(pContainer, pSlot, pX, pY);
        this.mechanicalTableRecipe = mechanicalTableRecipe;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return isValid(pStack);
    }

    public boolean isValid(ItemStack itemStack) {
        if (mechanicalTableRecipe == null || mechanicalTableRecipe.base == null) {
            log.info("No Base Recipe Found, Check the pPath");
            return false;
        }
        return itemStack.getItem().equals(mechanicalTableRecipe.getBaseItem().getItem());
    }
}
