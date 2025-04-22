package com.mattutos.arkfuture.crafting.recipe.mechanicalentityassembler;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record MechanicalEntityAssemblerRecipe(
        Ingredient core,
        List<Ingredient> components,
        Integer energyExpenditurePerTick,
        Integer totalTicksPerCraft,
        EntityType<?> entityType
) {

    public boolean matches(ItemStack core, List<ItemStack> parts) {
        if (!this.core().test(core)) return false;

        List<ItemStack> unmatchedParts = new ArrayList<>(parts);

        return this.components().stream()
                .allMatch(i -> unmatchedParts.stream()
                        .filter(p -> i.test(p))
                        .findFirst()
                        .map(unmatchedParts::remove)
                        .isPresent()
                );
    }
}
