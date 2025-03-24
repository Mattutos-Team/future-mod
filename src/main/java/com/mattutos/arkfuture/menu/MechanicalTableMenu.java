package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.common.crafting.recipe.MechanicalTableRecipe;
import com.mattutos.arkfuture.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class MechanicalTableMenu extends ItemCombinerMenu {
    @Nullable
    private CraftingRecipe craftingRecipe;
    private Level level;
    private List<MechanicalTableRecipe> recipes;

    public MechanicalTableMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public MechanicalTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access) {
        super(MenuInit.mechanical_table.get(), pContainerId, pPlayerInventory, access);
        this.level = pPlayerInventory.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.EXTREME_SMITHING_RECIPE.get());
    }

    @Override
    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return false;
    }

    @Override
    protected void onTake(Player pPlayer, ItemStack pStack) {

    }

    @Override
    protected boolean isValidBlock(BlockState pState) {
        return false;
    }

    @Override
    public void createResult() {

    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return null;
    }
}
