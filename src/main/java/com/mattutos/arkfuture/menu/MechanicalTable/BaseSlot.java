package com.mattutos.arkfuture.menu.MechanicalTable;

import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Slf4j
public class BaseSlot extends SlotItemHandler {

    private final List<IngredientStack.Item> validBaseItems;

    public BaseSlot(IItemHandler pContainer, int pSlot, int pX, int pY, List<IngredientStack.Item> validBaseItems) {
        super(pContainer, pSlot, pX, pY);
        this.validBaseItems = validBaseItems;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return isValid(pStack);
    }

    public boolean isValid(ItemStack itemStack) {
        if (validBaseItems == null || validBaseItems.isEmpty()) {
            log.info("No valid base ingredients found.");
            return false;
        }

        for (IngredientStack.Item validBase : validBaseItems) {
            if (validBase.getIngredient().test(itemStack)) {
                return true;
            }
        }

        log.info("Invalid base item: {}", itemStack.getItem());
        return false;
    }
}
