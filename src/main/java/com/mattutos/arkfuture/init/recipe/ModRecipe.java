package com.mattutos.arkfuture.init.recipe;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipe {
    // REGISTER SERIALIZER
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArkFuture.MOD_ID);

    // REGISTER TYPE
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArkFuture.MOD_ID);


    //MECHANICAL TABLE SERIALIZER
    public static final RegistryObject<MechanicalTableSerializer> MECHANICAL_TABLE_SERIALIZER =
            SERIALIZERS.register("mechanical_table", MechanicalTableSerializer::new);

    //MECHANICAL TABLE TPYE
    public static final RegistryObject<RecipeType<MechanicalTableRecipe>> MECHANICAL_TABLE_TYPE =
            TYPES.register("mechanical_table", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "mechanical_table";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
