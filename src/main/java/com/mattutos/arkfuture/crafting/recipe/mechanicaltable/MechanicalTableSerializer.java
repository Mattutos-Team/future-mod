package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import com.mattutos.arkfuture.crafting.recipe.common.GlodCodecs;
import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MechanicalTableSerializer implements RecipeSerializer<MechanicalTableRecipe> {
    public static final MapCodec<MechanicalTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                    ItemStack.CODEC.fieldOf("output").forGetter(MechanicalTableRecipe::getOutput),
                    IngredientStack.ITEM_CODEC.fieldOf("base").forGetter(MechanicalTableRecipe::getBase),
                    IngredientStack.ITEM_CODEC.fieldOf("mechanical_pliers").forGetter(MechanicalTableRecipe::getMechanicalPliers),
                    IngredientStack.ITEM_CODEC.listOf().fieldOf("input_items").forGetter(MechanicalTableRecipe::getInputs)
            ).apply(builder, MechanicalTableRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MechanicalTableRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            MechanicalTableRecipe::getOutput,
            IngredientStack.ITEM_STREAM_CODEC,
            MechanicalTableRecipe::getBase,
            IngredientStack.ITEM_STREAM_CODEC,
            MechanicalTableRecipe::getMechanicalPliers,
            GlodCodecs.list(IngredientStack.ITEM_STREAM_CODEC),
            MechanicalTableRecipe::getInputs,
            MechanicalTableRecipe::new
    );

    @Override
    public MapCodec<MechanicalTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MechanicalTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
