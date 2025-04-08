package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

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

import java.util.List;

public class MechanicalTableRecipe implements Recipe<RecipeInput> {
    private static final Logger log = LoggerFactory.getLogger(MechanicalTableRecipe.class);
    @Getter
    public final IngredientStack.Item base;

    @Getter
    public final IngredientStack.Item mechanicalPliers;

    @Getter
    public final Integer energyExpenditurePerTick;

    @Getter
    public final Integer maxTickPerCraft;

    protected List<IngredientStack.Item> inputItems;
    @Getter
    public final ItemStack output;

    public MechanicalTableRecipe(ItemStack pOutput, IngredientStack.Item pBase, IngredientStack.Item mechanicalPliers, List<IngredientStack.Item> pInputItems, Integer energyExpenditurePerTick, Integer maxTickPerCraft) {
        this.output = pOutput;
        this.base = pBase;
        this.mechanicalPliers = mechanicalPliers;
        this.inputItems = pInputItems;
        this.energyExpenditurePerTick = energyExpenditurePerTick == null ? 5 : energyExpenditurePerTick;
        this.maxTickPerCraft = maxTickPerCraft == null ? 50 : maxTickPerCraft;
        ;
    }

    public List<IngredientStack.Item> getInputs() {
        return this.inputItems;
    }


    @Override
    public boolean matches(RecipeInput pInput, Level pLevel) {
        int ingredientIndex = 0;

        for (int i = 0; i < pInput.size(); i++) {
            ItemStack stack = pInput.getItem(i);

            if (!stack.isEmpty()) {
                if (ingredientIndex < inputItems.size()) {
                    // CHECKING INPUT ITEMS (0-4 ARE THE INDEX MAPPED BY MECHANICAL TABLE RECIPE INPUT)
                    if (!inputItems.get(ingredientIndex).getIngredient().test(stack)) {
                        return false;
                    }
                    ingredientIndex++;
                } else {
                    // CHECKING BASE ITEM (4 IS THE INDEX MAPPED BY MECHANICAL TABLE RECIPE INPUT)
                    if (i == 4 && !base.getIngredient().test(stack)) {
                        log.info("Mechanical Table Recipe - Base not found");
                        return false;
                    }
                    // CHECKING MECHANICAL (6 IS THE INDEX MAPPED BY MECHANICAL TABLE RECIPE INPUT)
                    else if (i == 6 && !mechanicalPliers.getIngredient().test(stack)) {
                        log.info("Mechanical Table Recipe - Mechanical Pliers not found");
                        return false;
                    }
                }
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

    public boolean hasMechanicalPliers(@NotNull ItemStack pStack) {
        return this.mechanicalPliers.getIngredient().test(pStack);
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
