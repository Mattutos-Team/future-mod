package com.mattutos.arkfuture.crafting.recipe.MechanicalTable;

import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MechanicalTableRecipe implements Recipe<RecipeInput> {

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

    public List<IngredientStack<?, ?>> getSample() {
        List<IngredientStack<?, ?>> sample = new ArrayList<>();
        for (var in : this.inputItems) {
            if (!in.isEmpty()) {
                sample.add(in.sample());
            }
        }
        return sample;
    }

    @Override
    public boolean matches(RecipeInput pInput, Level pLevel) {
        int ingredientIndex = 0;
        for (int i = 0; i < pInput.size(); i++) {
            ItemStack stack = pInput.getItem(i);

            if (!stack.isEmpty()) {
                if (ingredientIndex >= inputItems.size()) {
                    return false;
                }

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

    public ItemStack getBaseItem() {
        return base.getIngredient().getItems()[0];
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
