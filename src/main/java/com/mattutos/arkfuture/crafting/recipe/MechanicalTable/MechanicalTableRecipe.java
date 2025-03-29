package com.mattutos.arkfuture.crafting.recipe.MechanicalTable;

import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MechanicalTableRecipe implements Recipe<RecipeInput> {

    private static final Logger log = LoggerFactory.getLogger(MechanicalTableRecipe.class);
    @Getter
    public final IngredientStack.Item base;
    protected List<IngredientStack.Item> inputItems;
    @Getter
    public final ItemStack output;

    public MechanicalTableRecipe(ItemStack pOutput, IngredientStack.Item pBase, List<IngredientStack.Item> pInputItems) {
        this.output = pOutput;
        this.base = pBase;
        this.inputItems = pInputItems;
    }

    public List<IngredientStack.Item> getInputs() {
        return this.inputItems;
    }


    @Override
    public boolean matches(RecipeInput pInput, Level pLevel) {
        int ingredientIndex = 0;

        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack stack = pInput.getItem(i);

            if (!stack.isEmpty()) {
                if (ingredientIndex >= inputItems.size()) {
                    return false;
                }

                //CHECKING WHETHER IS A VALID INGREDIENT OR NOT
                if (!inputItems.get(ingredientIndex).getIngredient().test(stack)) {
                    return false;
                }

                ingredientIndex++;
            }
        }

        return ingredientIndex == inputItems.size();
    }


    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput pContainer, @NotNull HolderLookup.Provider pRegistryAccess) {
        return getResultItem(pRegistryAccess).copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider pRegistryAccess) {
        return this.output;
    }

    public boolean isBaseIngredient(@NotNull ItemStack pStack) {
        return this.base.getIngredient().test(pStack);
    }

    public boolean isAdditionIngredient(@NotNull ItemStack pStack) {
        for (IngredientStack.Item addition : inputItems) {
            if (addition.getIngredient().test(pStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipe.MECHANICAL_TABLE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipe.MECHANICAL_TABLE_TYPE.get();
    }
}
