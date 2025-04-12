package com.mattutos.arkfuture.menu.common;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Slf4j
public class IngredientSlot extends SlotItemHandler {

    private final Set<Ingredient> validIngredientList;

    public IngredientSlot(IItemHandler pContainer, int pSlot, int pX, int pY, Set<Ingredient> validIngredientList) {
        super(pContainer, pSlot, pX, pY);
        this.validIngredientList = validIngredientList;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return isValid(pStack);
    }

    public boolean isValid(ItemStack itemStack) {
        if (validIngredientList == null || validIngredientList.isEmpty()) {
            log.warn("No valid ingredients found.");
            return false;
        }

        return validIngredientList.stream().anyMatch(ingredient -> ingredient.test(itemStack));
    }
}
