package com.mattutos.arkfuture.menu;

import com.mattutos.arkfuture.crafting.recipe.MechanicalTableRecipe;
import com.mattutos.arkfuture.init.BlockInit;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class MechanicalTableMenu extends ItemCombinerMenu {
    @Nullable
    private MechanicalTableRecipe selectedRecipe;
    private final Level level;
    private final List<RecipeHolder<MechanicalTableRecipe>> recipes;

    public MechanicalTableMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public MechanicalTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess access) {
        super(MenuInit.MECHANICAL_TABLE_MENU.get(), pContainerId, pPlayerInventory, access);
        this.level = pPlayerInventory.player.level();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipe.MECHANICAL_TABLE_TYPE.get());
    }

    @Override
    protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
        return this.selectedRecipe != null && this.selectedRecipe.matches((CraftingInput) this.inputSlots, this.level);
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
        List<RecipeHolder<MechanicalTableRecipe>> list = this.level.getRecipeManager().getAllRecipesFor(ModRecipe.MECHANICAL_TABLE_TYPE.get());
        if (list.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            MechanicalTableRecipe
                    mechanicalTableRecipe = list.getFirst().value();

            ItemStack itemstack = mechanicalTableRecipe.assemble((RecipeInput) this.inputSlots, this.level.registryAccess());

            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.selectedRecipe = mechanicalTableRecipe;
//                this.resultSlots.setRecipeUsed(mechanicalTableRecipe);
                this.resultSlots.setItem(0, itemstack);
            }
        }
    }

    @Override
    protected @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 31, 35, (stack) -> {
            return this.recipes.stream().anyMatch((recipe) -> {
                return recipe.value().isAdditionIngredient(stack);
            });
        }).withSlot(1, 49, 35, (stack) -> {
            return this.recipes.stream().anyMatch((recipe) -> {
                return recipe.value().isBaseIngredient(stack);
            });
        }).withSlot(2, 67, 35, (stack) -> {
            return this.recipes.stream().anyMatch((recipe) -> {
                return recipe.value().isAdditionIngredient(stack);
            });
        }).withSlot(3, 49, 17, (stack) -> {
            return this.recipes.stream().anyMatch((recipe) -> {
                return recipe.value().isAdditionIngredient(stack);
            });
        }).withSlot(4, 49, 53, (stack) -> {
            return this.recipes.stream().anyMatch((recipe) -> {
                return recipe.value().isAdditionIngredient(stack);
            });
        }).withResultSlot(5, 121, 35).build();
    }


    @Override
    public int getSlotToQuickMoveTo(@NotNull ItemStack pStack) {
        return this.recipes.stream().map((mechanicalTableRecipeRecipeHolder) -> {
            return findSlotMatchingIngredient(mechanicalTableRecipeRecipeHolder, pStack);
        }).filter(Optional::isPresent).findFirst().orElse(Optional.of(List.of(0))).get().get(0);
    }

    private static Optional<List<Integer>> findSlotMatchingIngredient(RecipeHolder<MechanicalTableRecipe> pRecipe, ItemStack pStack) {
        if (pRecipe.value().isBaseIngredient(pStack)) {
            return Optional.of(List.of(1));
        } else {
            return pRecipe.value().isAdditionIngredient(pStack) ? Optional.of(List.of(0, 2, 3, 4)) : Optional.empty();
        }
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.inputSlots.getItem(0), this.inputSlots.getItem(1), this.inputSlots.getItem(2), this.inputSlots.getItem(3), this.inputSlots.getItem(4));
    }


    private void shrinkStackInSlot(int pIndex) {
        ItemStack itemstack = this.inputSlots.getItem(pIndex);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
            this.inputSlots.setItem(pIndex, itemstack);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public boolean canMoveIntoInputSlots(@NotNull ItemStack pStack) {
        return this.recipes.stream().map((mechanicalTableRecipeRecipeHolder) -> {
            return findSlotMatchingIngredient(mechanicalTableRecipeRecipeHolder, pStack);
        }).anyMatch(Optional::isPresent);
    }
}
