package com.mattutos.arkfuture.crafting.recipe.mechanicaltable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MechanicalTableSerializer implements RecipeSerializer<MechanicalTableRecipe> {
    public static final MapCodec<MechanicalTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(MechanicalTableRecipe::base),
            Ingredient.CODEC_NONEMPTY.fieldOf("pliers").forGetter(MechanicalTableRecipe::pliers),
            Ingredient.CODEC_NONEMPTY.fieldOf("additions").forGetter(MechanicalTableRecipe::additions),
            Codec.INT.fieldOf("energy_expenditure_per_tick").forGetter(MechanicalTableRecipe::energyExpenditurePerTick),
            Codec.INT.fieldOf("total_ticks_per_craft").forGetter(MechanicalTableRecipe::totalTicksPerCraft),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(MechanicalTableRecipe::output)
    ).apply(inst, MechanicalTableRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MechanicalTableRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, MechanicalTableRecipe::base,
                    Ingredient.CONTENTS_STREAM_CODEC, MechanicalTableRecipe::pliers,
                    Ingredient.CONTENTS_STREAM_CODEC, MechanicalTableRecipe::additions,
                    ByteBufCodecs.INT, MechanicalTableRecipe::energyExpenditurePerTick,
                    ByteBufCodecs.INT, MechanicalTableRecipe::totalTicksPerCraft,
                    ItemStack.STREAM_CODEC, MechanicalTableRecipe::output,
                    MechanicalTableRecipe::new);

    @Override
    public MapCodec<MechanicalTableRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MechanicalTableRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
