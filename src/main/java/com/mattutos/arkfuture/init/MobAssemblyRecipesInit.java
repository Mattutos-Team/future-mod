package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.crafting.recipe.mechanicalentityassembler.MechanicalEntityAssemblerRecipe;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class MobAssemblyRecipesInit {
    public static final List<MechanicalEntityAssemblerRecipe> RECIPES = new ArrayList<>();

    public static void register() {
        RECIPES.add(new MechanicalEntityAssemblerRecipe(
                Ingredient.of(Items.GOLDEN_APPLE),
                List.of(Ingredient.of(Items.LEATHER), Ingredient.of(Items.LEATHER), Ingredient.of(Items.LEATHER), Ingredient.of(Items.MILK_BUCKET)),
                10,
                200,
                EntityType.COW
        ));
    }

    public static MechanicalEntityAssemblerRecipe findMatching(ItemStack core, List<ItemStack> parts) {
        return RECIPES.stream()
                .filter(r -> r.matches(core, parts))
                .findFirst()
                .orElse(null);
    }
}
