package com.mattutos.arkfuture.crafting.recipe.explosion;

import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ExplosionRecipeSerializer implements RecipeSerializer<ExplosionRecipe> {

    public static final MapCodec<ExplosionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredients").forGetter(ExplosionRecipe::ingredients),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(ExplosionRecipe::output)
    ).apply(inst, ExplosionRecipe::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, ExplosionRecipe::ingredients,
                    ItemStack.STREAM_CODEC, ExplosionRecipe::output,
                    ExplosionRecipe::new);

    @Override
    public MapCodec<ExplosionRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ExplosionRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
