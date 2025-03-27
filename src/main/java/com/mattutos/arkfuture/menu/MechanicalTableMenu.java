package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.custom.crafting.recipe.MechanicalTableRecipe;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MechanicalTableMenu extends ItemCombinerMenu {
    @Nullable
    private CustomRecipe customRecipe;
    private Level level;
    private List<RecipeHolder<MechanicalTableRecipe>> recipes;

    public MechanicalTableMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public MechanicalTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access) {
        super(MenuInit.mechanical_table.get(), pContainerId, pPlayerInventory, access);
        this.level = pPlayerInventory.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(RecipeTypeInit.MECHANICAL_TABLE_RECIPE.get());
    }

    @Override
    protected boolean mayPickup(@NotNull Player pPlayer, boolean pHasStack) {
        return this.customRecipe != null && this.customRecipe.matches((CraftingInput) this.inputSlots, this.level);
    }

    @Override
    protected void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
        pStack.onCraftedBy(pPlayer.level(), pPlayer, pStack.getCount());
        this.resultSlots.awardUsedRecipes(pPlayer, this.getRelevantItems());
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);
        this.shrinkStackInSlot(2);
        this.shrinkStackInSlot(3);
        this.shrinkStackInSlot(4);
        this.access.execute((level, pos) -> {
            level.levelEvent(1044, pos, 0);
        });
    }

    @Override
    protected boolean isValidBlock(@NotNull BlockState pState) {
        return pState.is(BlockInit.MECHANICAL_TABLE.get());
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public void createResult() {
        List<MechanicalTableRecipe> list = this.level.getRecipeManager().getRecipesFor(RecipeTypeInit.MECHANICAL_TABLE_RECIPE.get(), this.inputSlots, this.level);
        if (list.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            MechanicalTableRecipe mechanicalTableRecipe = list.get(0);
            ItemStack itemstack = mechanicalTableRecipe.assemble((CraftingInput) this.inputSlots, this.level.registryAccess());
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.customRecipe = mechanicalTableRecipe;
                this.resultSlots.setRecipeUsed(mechanicalTableRecipe);
                this.resultSlots.setItem(0, itemstack);
            }
        }
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return null;
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1), this.inputSlots.getItem(2), this.inputSlots.getItem(3), this.inputSlots.getItem(4));
    }

    // this method is used to shrink the item in craft slot
    private void shrinkStackInSlot(int pIndex) {
        ItemStack itemstack = this.inputSlots.getItem(pIndex);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
            this.inputSlots.setItem(pIndex, itemstack);
        }
    }
}
