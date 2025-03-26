package com.mattutos.arkfuture.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MechanicalTableRecipe extends CustomRecipe {
    private final ResourceLocation id;
    //    public final Ingredient template; --> we can implement this to future tiers on mechanical table
    public final Ingredient base;
    public final Ingredient additions;
    public final ItemStack result;

    public MechanicalTableRecipe(ResourceLocation pId, Ingredient pBase, Ingredient additions, ItemStack pResult, CraftingBookCategory pCategory) {
        super(pCategory);
        this.id = pId;
        this.base = pBase;
        this.additions = additions;
        this.result = pResult;
    }

    @Override
    public boolean matches(CraftingInput pInput, Level pLevel) {
        return this.additions.test(pInput.getItem(0)) && this.base.test(pInput.getItem(1))
                && this.additions.test(pInput.getItem(2))
                && this.additions.test(pInput.getItem(3))
                && this.additions.test(pInput.getItem(4));
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    public boolean isBaseIngredient(@NotNull ItemStack pStack) {
        return this.base.test(pStack);
    }

    public boolean isAdditionIngredient(@NotNull ItemStack pStack) {
        return this.additions.test(pStack);
    }

    public @NotNull ResourceLocation getId() {
        return this.id;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
