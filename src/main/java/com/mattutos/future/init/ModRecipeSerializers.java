package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import com.mattutos.future.crafting.recipe.VarnishedWoodRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FutureMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> VARNISHED_WOOD = register("varnished_wood", VarnishedWoodRecipe.Serializer::new);

    @SubscribeEvent
    public void onRegisterSerializers(RegisterEvent event) {
//        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, registry -> {
//            CraftingHelper.register()
//        });
    }

    private static RegistryObject<RecipeSerializer<?>> register(String name, Supplier<RecipeSerializer<?>> serializer) {
        return REGISTRY.register(name, serializer);
    }

}
