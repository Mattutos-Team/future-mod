package com.mattutos.arkfuture.crafting.recipe;

import com.mattutos.arkfuture.crafting.recipe.common.IngredientStack;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MechanicalTableRecipe implements Recipe<RecipeInput> {
    public final IngredientStack.Item base;
    private final List<IngredientStack.Item> inputItems;
    private final ItemStack output;


    public MechanicalTableRecipe(IngredientStack.Item pBase, List<IngredientStack.Item> pInputItems, ItemStack pOutput) {
        this.base = pBase;
        this.inputItems = pInputItems;
        this.output = pOutput;

    }


    @Override
    public boolean matches(RecipeInput pInput, Level pLevel) {
        int ingredientIndex = 0;
        for (int i = 0; i < pInput.size(); i++) {
            ItemStack stack = pInput.getItem(i);

            if (!stack.isEmpty()) {
                if (ingredientIndex >= inputItems.size()) {
                    return false;
                }

                if (!inputItems.get(ingredientIndex).getIngredient().test(stack)) {
                    return false;
                }

                ingredientIndex++;
            }
        }

        return ingredientIndex == inputItems.size();
    }


    @Override
    public ItemStack assemble(RecipeInput pInput, HolderLookup.Provider pRegistries) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.output;
    }

    public boolean isBaseIngredient(@NotNull ItemStack pStack) {
        return this.base.getIngredient().test(pStack);
    }

    public boolean isAdditionIngredient(@NotNull ItemStack pStack) {
        for (IngredientStack.Item addition : inputItems) {
            if (addition.getIngredient().test(pStack)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipe.MECHANICAL_TABLE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipe.MECHANICAL_TABLE_TYPE.get();
    }

    // Getter to inputItems
    public List<IngredientStack.Item> inputItems() {
        return inputItems;
    }

    // Getter  (output)
    public ItemStack output() {
        return output;
    }


    public static class Serializer implements RecipeSerializer<MechanicalTableRecipe> {

        //SHOULD FOLLOW THE SAME CONSTRUCTOR ORDER
        public final static MapCodec<MechanicalTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        IngredientStack.ITEM_CODEC.fieldOf("base").forGetter(ir -> ir.base),
                        IngredientStack.ITEM_CODEC.listOf().fieldOf("input_items").forGetter(ir -> ir.inputItems),
                        ItemStack.CODEC.fieldOf("output").forGetter(ir -> ir.output)

                ).apply(builder, MechanicalTableRecipe::new)
        );

        @Override
        public MapCodec<MechanicalTableRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MechanicalTableRecipe> streamCodec() {
            return null;
        }
    }

}
