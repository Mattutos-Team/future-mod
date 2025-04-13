package com.mattutos.arkfuture.crafting.recipe.transform;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TransformRecipeSerializer implements RecipeSerializer<TransformRecipe> {

    public static final MapCodec<TransformRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        return builder.group(
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(ingredients -> {
                                    return DataResult
                                            .success(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new)));
                                }, DataResult::success)
                                .forGetter(TransformRecipe::ingredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(TransformRecipe::output),
                        TransformCircumstance.CODEC
                                .optionalFieldOf("circumstance", TransformCircumstance.explosion())
                                .forGetter(TransformRecipe::circumstance))
                .apply(builder, TransformRecipe::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, TransformRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)),
            TransformRecipe::getIngredients,
            ItemStack.STREAM_CODEC,
            TransformRecipe::output,
            TransformCircumstance.STREAM_CODEC,
            TransformRecipe::circumstance,
            TransformRecipe::new);

    @Override
    public MapCodec<TransformRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TransformRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
