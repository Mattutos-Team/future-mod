package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.common.crafting.recipe.MechanicalTableRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class RecipeTypeInit {
    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArkFuture.MOD_ID);


    public static final @NotNull RegistryObject<RecipeType<MechanicalTableRecipe>> MECHANICAL_TABLE_RECIPE = registerRecipe("mechanical_table_recipe", () -> RecipeType.simple(new ResourceLocation(ArkFuture.MOD_ID, "mechanical_table_recipe")));

    public static <T extends Recipe<Container>> RegistryObject<RecipeType<T>> registerRecipe(String name, Supplier<RecipeType<T>> type) {
        return REGISTRY.register(name, type);
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
