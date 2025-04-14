package com.mattutos.arkfuture.datagen.custom;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class MechanicalTableRecipeBuilder implements RecipeBuilder {

    private final MechanicalTableRecipe.Factory<?> factory;
    private final Item result;
    private final Integer count;
    private Item base;
    private Item pliers;
    private Item[] additions;
    private Integer energyExpenditurePerTick;
    private Integer totalTicksPerCraft;

    private MechanicalTableRecipeBuilder(MechanicalTableRecipe.Factory<?> factory, ItemLike base, ItemLike pliers, ItemLike[] additions, Integer energyExpenditurePerTick, Integer totalTicksPerCraft, ItemLike result, int count) {
        this.factory = factory;
        this.base = base.asItem();
        this.pliers = pliers.asItem();
        this.additions = (Item[]) Arrays.stream(additions).map(ItemLike::asItem).toArray();
        this.energyExpenditurePerTick = energyExpenditurePerTick;
        this.totalTicksPerCraft = totalTicksPerCraft;
        this.result = result.asItem();
        this.count = count;
    }

    private MechanicalTableRecipeBuilder(MechanicalTableRecipe.Factory<?> factory, ItemLike result, int count) {
        this.factory = factory;
        this.result = result.asItem();
        this.count = count;
    }

    private MechanicalTableRecipeBuilder(ItemLike base, ItemLike pliers, ItemLike[] additions, Integer energyExpenditurePerTick, Integer totalTicksPerCraft, ItemLike result, int count) {
        this(MechanicalTableRecipe::new, base, pliers, additions, energyExpenditurePerTick, totalTicksPerCraft, result, count);
    }

    private MechanicalTableRecipeBuilder(ItemLike result, int count) {
        this(MechanicalTableRecipe::new, result, count);
    }

    public static MechanicalTableRecipeBuilder allMechanicalTableRecipe(ItemLike base, ItemLike pliers, ItemLike[] additions, Integer energyExpenditurePerTick, Integer totalTicksPerCraft, ItemLike result, int count) {
        return new MechanicalTableRecipeBuilder(base, pliers, additions, energyExpenditurePerTick, totalTicksPerCraft, result, count);
    }

    public static MechanicalTableRecipeBuilder resultRecipe(ItemLike result, int count) {
        return new MechanicalTableRecipeBuilder(result, count);
    }

    public MechanicalTableRecipeBuilder requireBase(ItemLike base) {
        this.base = base.asItem();
        return this;
    }

    public MechanicalTableRecipeBuilder requirePliers(ItemLike pliers) {
        this.pliers = pliers.asItem();
        return this;
    }

    public MechanicalTableRecipeBuilder requireAdditions(ItemLike... additions) {
        this.additions = Arrays.stream(additions).map(ItemLike::asItem).toArray(Item[]::new);
        return this;
    }

    public MechanicalTableRecipeBuilder requireEnergyExpenditurePerTick(Integer energyExpenditurePerTick) {
        this.energyExpenditurePerTick = energyExpenditurePerTick;
        return this;
    }

    public MechanicalTableRecipeBuilder requireTotalTicksPerCraft(Integer totalTicksPerCraft) {
        this.totalTicksPerCraft = totalTicksPerCraft;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput pRecipeOutput) {
        ResourceLocation pId = ResourceLocation.fromNamespaceAndPath(ArkFuture.MOD_ID, "mechanical_table/");
        ResourceLocation itemId = RecipeBuilder.getDefaultRecipeId(this.result);
        this.save(pRecipeOutput, pId.withSuffix(itemId.getPath()));
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        this.ensureValid(pId);

        MechanicalTableRecipe mechanicalTableRecipe = this.factory.create(Ingredient.of(this.base),
                Ingredient.of(this.pliers),
                Ingredient.of(this.additions),
                this.energyExpenditurePerTick,
                this.totalTicksPerCraft,
                new ItemStack(this.result, this.count));

        pRecipeOutput.accept(pId, mechanicalTableRecipe, null);
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.result == null || this.count < 1) {
            throw new IllegalStateException("No result for " + pId);
        }
        if (this.base == null) {
            throw new IllegalStateException("No base for " + pId);
        }
        if (this.pliers == null) {
            throw new IllegalStateException("No pliers for " + pId);
        }
        if (this.additions == null) {
            throw new IllegalStateException("No additions for " + pId);
        }
        if (this.energyExpenditurePerTick == null) {
            throw new IllegalStateException("No energy expenditure per tick for " + pId);
        }
        if (this.totalTicksPerCraft == null) {
            throw new IllegalStateException("No total ticks per craft for " + pId);
        }
    }
}
