package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity;
import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity.DATA;
import com.mattutos.arkfuture.block.entity.MechanicalTableBlockEntity.SLOT;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.core.inventory.SimpleEnumContainerData;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.menu.common.IngredientSlot;
import com.mattutos.arkfuture.menu.common.ResultSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MechanicalTableMenu extends AFAbstractContainerMenu<DATA> {

    public MechanicalTableMenu(int pContainerId, Inventory pInventory, FriendlyByteBuf pExtraData) {
        this(pContainerId, pInventory, pInventory.player.level().getBlockEntity(pExtraData.readBlockPos()), new SimpleEnumContainerData<>(DATA.class));
    }

    public MechanicalTableMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity pBlockEntity, EnumContainerData<DATA> pContainerData) {
        super(MenuInit.MECHANICAL_TABLE_MENU.get(), pContainerId, pPlayerInventory, pBlockEntity, pContainerData);

        List<RecipeHolder<MechanicalTableRecipe>> allRecipesForMechanicalTable = this.level.getRecipeManager().getAllRecipesFor(ModRecipe.MECHANICAL_TABLE_TYPE.get());
        Set<Ingredient> validBaseIngredientsList = getValidBaseIngredients(allRecipesForMechanicalTable);
        Set<Ingredient> validPliersIngredientsList = getValidPliersIngredients(allRecipesForMechanicalTable);
        Set<Ingredient> validAdditionsIngredients = getValidAdditionsIngredients(allRecipesForMechanicalTable);

        addPlayerInventorySlots(pPlayerInventory);

        ItemStackHandler itemStackHandler = ((MechanicalTableBlockEntity) pBlockEntity).getItemStackHandler();
        //BASE ITEM
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.BASE.ordinal(), 29, 35, validBaseIngredientsList));

        //MECHANICAL PLIERS
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.PLIERS.ordinal(), 84, 35, validPliersIngredientsList));

        //INGREDIENTS ITEMS
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.INGREDIENT_1.ordinal(), 11, 35, validAdditionsIngredients)); // LEFT
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.INGREDIENT_2.ordinal(), 47, 35, validAdditionsIngredients)); // RIGHT
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.INGREDIENT_3.ordinal(), 29, 17, validAdditionsIngredients)); // UP
        this.addSlot(new IngredientSlot(itemStackHandler, SLOT.INGREDIENT_4.ordinal(), 29, 53, validAdditionsIngredients)); // BOTTOM

        //RESULT SLOT
        this.addSlot(new ResultSlot(itemStackHandler, SLOT.OUTPUT.ordinal(), 152, 35));

        addDataSlots(containerData);
    }

    //CHECK WHETHER IS CRAFTING OR NOT
    public boolean isCrafting() {
        return containerData.get(DATA.PROGRESS) > 0;
    }

    public boolean isEnergyIncreasing() {
        long energy = containerData.get(DATA.ENERGY_STORED);
        return energy <= containerData.get(DATA.MAX_ENERGY_CAPACITY);
    }

    public float getScaledEnergyStoredProgress() {
        long progress = containerData.get(DATA.ENERGY_STORED);
        long maxProgress = containerData.get(DATA.MAX_ENERGY_CAPACITY);

        return maxProgress != 0 && progress != 0 ? ((float) progress / (float) maxProgress) : 0;
    }

    public float getScaledArrowProgress() {
        long progress = this.containerData.get(DATA.PROGRESS);
        long maxProgress = this.containerData.get(DATA.MAX_PROGRESS);

        return maxProgress != 0 && progress != 0 ? ((float) progress / (float) maxProgress) : 0;
    }

    private Set<Ingredient> getValidBaseIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::base)
                .filter(base -> base != null && !base.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Ingredient> getValidAdditionsIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::additions)
                .filter(addition -> addition != null && !addition.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<Ingredient> getValidPliersIngredients(List<RecipeHolder<MechanicalTableRecipe>> recipes) {
        return recipes.stream()
                .map(RecipeHolder::value)
                .map(MechanicalTableRecipe::pliers)
                .filter(pliers -> pliers != null && !pliers.isEmpty())
                .collect(Collectors.toSet());
    }

    public long getStoredEnergy() {
        return containerData.get(DATA.ENERGY_STORED);
    }

    public long getMaxEnergy() {
        return containerData.get(DATA.MAX_ENERGY_CAPACITY);
    }
}
