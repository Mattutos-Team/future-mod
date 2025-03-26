package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.common.crafting.recipe.MechanicalTableRecipe;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.common.crafting.ICompressorRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.BaseTableCraftingRecipe;
import committee.nova.mods.avaritia.common.crafting.recipe.ExtremeSmithingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RecipeTypeInit {
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArkFuture.MOD_ID);

    public static final @NotNull RegistryObject<RecipeType<MechanicalTableRecipe>> MECHANICAL_TABLE_RECIPE = recipe("mechanical_table_recipe", () -> RecipeType.simple(new ResourceLocation(ArkFuture.MOD_ID, "mechanical_table_recipe")));


    public static <T extends Recipe<RecipeInput>> RegistryObject<RecipeType<T>> recipe(String name, Supplier<RecipeType<T>> type) {
        return RECIPES.register(name, type);
    }

}