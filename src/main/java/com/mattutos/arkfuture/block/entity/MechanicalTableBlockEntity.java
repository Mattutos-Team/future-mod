package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.MechanicalTable.MechanicalTableRecipeInput;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.menu.MechanicalTable.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MechanicalTableBlockEntity extends BlockEntity implements MenuProvider {
    private static final int INPUT_SLOT_1 = 0;  // First input slot
    private static final int INPUT_SLOT_2 = 1;  // Second input slot
    private static final int INPUT_SLOT_3 = 2;  // Third input slot
    private static final int INPUT_SLOT_4 = 3;  // Fourth input slot
    private static final int INPUT_SLOT_5 = 4;  // Fifth input slot
    private static final int OUTPUT_SLOT = 5;   // Output slot

    private int progress = 0;
    private int maxProgress = 72;

    public final ItemStackHandler inventory = new ItemStackHandler(6) { //5 TO CRAFT +1 TO OUTPUT
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };


    public MechanicalTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_TABLE.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mechanical Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MechanicalTableMenu(pContainerId, pPlayerInventory, this);
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void craftItem() {
        Optional<RecipeHolder<MechanicalTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            MechanicalTableRecipe currentRecipe = recipe.get().value();


            // Extract 1 item from each of the 5 input slots (indices 0 to 4)
            for (int i = 0; i < 5; i++) {
                inventory.extractItem(i, 1, true);  // Extract 1 item from each input slot
            }

            // Optional: Reset output slot before placing new item (if needed)
            inventory.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);

            // Add the crafted output to the output slot
            ItemStack output = currentRecipe.getOutput();
            inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                    inventory.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
        }
    }


    private boolean hasRecipe() {
        Optional<RecipeHolder<MechanicalTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        // Check all the input slots (all 5)
        MechanicalTableRecipe currentRecipe = recipe.get().value();
        boolean canCraft = true;
        for (int i = 0; i < 5; i++) {
            ItemStack inputSlotStack = inventory.getStackInSlot(i);

            if (!currentRecipe.isBaseIngredient(inputSlotStack)) {
                canCraft = false;
                break;
            }

            if (!currentRecipe.isAdditionIngredient(inputSlotStack)) {
                canCraft = false;
                break;
            }
        }

        // Check if the recipe is valid and if we can fit the output in the output slot
        ItemStack output = currentRecipe.getOutput();
        return canCraft && canInsertItemIntoOutputSlot(output) && canInsertAmountIntoOutputSlot(output.getCount());
    }

    private Optional<RecipeHolder<MechanicalTableRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipe.MECHANICAL_TABLE_TYPE.get(), new MechanicalTableRecipeInput(
                        inventory.getStackInSlot(INPUT_SLOT_1),
                        inventory.getStackInSlot(INPUT_SLOT_2),
                        inventory.getStackInSlot(INPUT_SLOT_3),
                        inventory.getStackInSlot(INPUT_SLOT_4),
                        inventory.getStackInSlot(INPUT_SLOT_5)), level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.inventory.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = inventory.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : inventory.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }
}
