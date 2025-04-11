package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import com.mattutos.arkfuture.init.recipe.ModRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record MechanicalTableRecipe(Ingredient base,
                                    Ingredient pliers,
                                    Ingredient additions,
                                    Integer energyExpenditurePerTick,
                                    Integer totalTicksPerCraft,
                                    ItemStack output) implements Recipe<MechanicalTableRecipeInput> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(base);
        list.add(pliers);
        list.add(additions);
        return list;
    }

    @Override
    public boolean matches(MechanicalTableRecipeInput pInput, Level pLevel) {
        if (pLevel.isClientSide()) return false;

        for (Ingredient ingredient : getIngredients()) {
            for (int i = 0; i < pInput.size(); i++) {
                ItemStack stack = pInput.getItem(i);
                if (stack.isEmpty() || !ingredient.test(stack)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(MechanicalTableRecipeInput pInput, HolderLookup.Provider pRegistries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.MECHANICAL_TABLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipe.MECHANICAL_TABLE_TYPE.get();
    }
}
