package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.core.inventory.BaseData;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipeInput;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.item.MechanicalPliersItem;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MechanicalTableBlockEntity extends AFEnergyContainerBlockEntity {

    public enum DATA implements BaseData {
        PROGRESS,
        MAX_PROGRESS,
        ENERGY_STORED(4),
        MAX_ENERGY_CAPACITY(4);

        private final int dataPackShort;

        DATA() {
            this.dataPackShort = 1;
        }

        DATA(int dataPackShort) {
            this.dataPackShort = dataPackShort;
        }

        @Override
        public int getDataPack() {
            return this.dataPackShort;
        }
    }

    private static final int OUTPUT_SLOT = 5;   //RESULT SLOT

    private final ItemStackHandler itemStackHandler = createItemHandler();
    private final AFEnergyStorage energyStorage = createEnergyStorage();
    private final EnumContainerData<DATA> containerData = createEnumContainerData();

    //DATA FOR PROGRESS BAR
    private int progress = 0;
    private int maxProgress;

    //TODO - DEFINE MAX TABLE ENERGY CAPACITY BASED ON THE TABLE TIER
    public static final int CAPACITY = 5_000;

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(7) { //5 TO CRAFT +1 TO MECHANICAL PLIERS +1 TO OUTPUT
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };
    }

    private @NotNull AFEnergyStorage createEnergyStorage() {
        return new AFEnergyStorage(CAPACITY);
    }

    private @NotNull EnumContainerData<DATA> createEnumContainerData() {
        return new EnumContainerData<>(DATA.class) {
            @Override
            public void set(DATA enumData, long value) {
            }

            @Override
            public long get(DATA enumData) {
                return switch (enumData) {
                    case PROGRESS -> MechanicalTableBlockEntity.this.progress;
                    case MAX_PROGRESS -> MechanicalTableBlockEntity.this.maxProgress;
                    case ENERGY_STORED -> energyStorage.getEnergyStored();
                    case MAX_ENERGY_CAPACITY -> energyStorage.getMaxEnergyStored();
                };
            }
        };
    }

    public MechanicalTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.MECHANICAL_TABLE.get(), pPos, pBlockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new MechanicalTableMenu(pContainerId, pInventory, this, this.containerData);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.ark_future.mechanical_table");
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    protected AFEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemStackHandler.serializeNBT(pRegistries));
        pTag.putInt("mechanical_table.progress", progress);
        pTag.putInt("mechanical_table.max_progress", maxProgress);
        pTag.put("mechanical_table.energy_stored", energyStorage.serializeNBT(pRegistries));
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemStackHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("mechanical_table.progress");
        maxProgress = pTag.getInt("mechanical_table.max_progress");
        energyStorage.deserializeNBT(pRegistries, pTag.get("mechanical_table.energy_stored"));
    }

    @Override
    public void tickServer() {
        Optional<RecipeHolder<MechanicalTableRecipe>> optionalCurrentRecipe = getCurrentRecipe();

        if (optionalCurrentRecipe.isEmpty() || !validateRecipe()) {
            resetProgress();
            return;
        }


        Integer energyExpenditurePerTick = optionalCurrentRecipe.get().value().energyExpenditurePerTick;
        if (energyStorage.extractEnergy(energyExpenditurePerTick, true) != energyExpenditurePerTick)
            return;

        if (validateRecipe()) {
            increaseCraftingProgress();

            //SETTING MAX PROGRESS BASED ON RECIPE
            maxProgress = optionalCurrentRecipe.get().value().maxTickPerCraft;

            //ENERGY EXPENDITURE PER TICK (MULTIPLE THIS EXTRACT VALUE BY MAX PROGRESS CRAFT (50))
            energyStorage.extractEnergy(energyExpenditurePerTick, false);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                setChanged(level, this.getBlockPos(), this.getBlockState());
            }
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
            ItemStack output = recipe.get().value().getOutput();
            ItemStack mechanicalPliers = itemStackHandler.getStackInSlot(6);

            if (mechanicalPliers.getItem() instanceof MechanicalPliersItem) {
                mechanicalPliers.hurtAndBreak(1, (ServerLevel) this.level, null, item -> {});
            }

            //HERE IS FIVE (5) CAUSE IT JUST BEING CONSIDERED THE 5 PRINCIPAL SLOTS TO CRAFT
            for (int i = 0; i < 5; i++) {
                if (itemStackHandler.extractItem(i, 1, false).isEmpty()) return;
            }

            ItemStack stackOutput = new ItemStack(output.getItem(), output.getCount());
            itemStackHandler.insertItem(OUTPUT_SLOT, stackOutput, false);
        }
    }

    private boolean validateRecipe() {
        Optional<RecipeHolder<MechanicalTableRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        MechanicalTableRecipe currentRecipe = recipe.get().value();
        // LOOP FOR ALL 6 AVAILABLE SLOTS (4 TO INGREDIENTS + 1 TO BASE + 1 TO MECHANICAL PLIERS)
        for (int i = 0; i <= 6; i++) {
            ItemStack inputSlotStack = itemStackHandler.getStackInSlot(i);

            // BASE ITEM IS INDEX 1
            if (i == 1) {
                if (!currentRecipe.isBaseIngredient(inputSlotStack)) {
                    return false;
                }
            }
            // MECHANICAL PLIERS ITEM IS INDEX 6
            else if (i == 6) {
                if (!currentRecipe.hasMechanicalPliers(inputSlotStack)) {
                    return false;
                }
            }
            // FROM 0 TO 4 ( REMOVING INDEX 1 ) ARE ALL INGREDIENTS SLOTS
            else {
                int[] ingredientSlots = {0, 2, 3, 4};
                for (int slot : ingredientSlots) {
                    if (i == slot) {
                        if (!currentRecipe.isAdditionIngredient(inputSlotStack)) {
                            return false;
                        }
                    }
                }
            }
        }

        ItemStack output = currentRecipe.getOutput();
        return canInsertItemIntoOutputSlot(output) && canInsertAmountIntoOutputSlot(output.getCount());
    }

    private Optional<RecipeHolder<MechanicalTableRecipe>> getCurrentRecipe() {
        MechanicalTableRecipeInput recipeInput = new MechanicalTableRecipeInput(itemStackHandler);

        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipe.MECHANICAL_TABLE_TYPE.get(), recipeInput, level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemStackHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

}
