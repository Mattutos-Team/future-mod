package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.block.entity.util.AFEnergyContainerBlockEntity;
import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.core.inventory.BaseData;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MechanicalTableBlockEntity extends AFEnergyContainerBlockEntity {

    public enum SLOT {
        BASE,
        PLIERS,
        INGREDIENT_1,
        INGREDIENT_2,
        INGREDIENT_3,
        INGREDIENT_4,
        OUTPUT;

        static public int count() {
            return SLOT.values().length;
        }
    }

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

    private final ItemStackHandler itemStackHandler;
    private final AFEnergyStorage energyStorage;
    private final EnumContainerData<DATA> containerData;

    //DATA FOR PROGRESS BAR
    private int progress = 0;
    private int maxProgress;

    //TODO - DEFINE MAX TABLE ENERGY CAPACITY BASED ON THE TABLE TIER
    public static final int CAPACITY = 5_000;

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

        itemStackHandler = createItemStackHandler(SLOT.count());
        energyStorage = createEnergyStorage(CAPACITY);
        containerData = createEnumContainerData();
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
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        pTag.putInt("mechanical_table.progress", progress);
        pTag.putInt("mechanical_table.max_progress", maxProgress);
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        progress = pTag.getInt("mechanical_table.progress");
        maxProgress = pTag.getInt("mechanical_table.max_progress");
    }

    @Override
    public void tickServer() {
        Optional<RecipeHolder<MechanicalTableRecipe>> optionalCurrentRecipe = getCurrentRecipe();

        if (optionalCurrentRecipe.isEmpty()) {
            resetProgress();
            return;
        }
        RecipeHolder<MechanicalTableRecipe> mechanicalTableRecipeRecipeHolder = optionalCurrentRecipe.get();

        if (!canCraft(mechanicalTableRecipeRecipeHolder)) return;

        //SETTING MAX PROGRESS BASED ON RECIPE
        maxProgress = mechanicalTableRecipeRecipeHolder.value().totalTicksPerCraft();

        Integer energyExpenditurePerTick = mechanicalTableRecipeRecipeHolder.value().energyExpenditurePerTick();
        if (energyStorage.extractEnergy(energyExpenditurePerTick, true) != energyExpenditurePerTick)
            return;

        //ENERGY EXPENDITURE PER TICK
        energyStorage.extractEnergy(energyExpenditurePerTick, false);

        increaseCraftingProgress();

        if (hasCraftingFinished()) {
            craftItem(mechanicalTableRecipeRecipeHolder);
            resetProgress();
        }
        setChanged();
    }

    private boolean canCraft(RecipeHolder<MechanicalTableRecipe> recipe) {
        return itemStackHandler.insertItem(SLOT.OUTPUT.ordinal(), recipe.value().output(), true).isEmpty();
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
        if (this.progress > this.maxProgress) this.progress = this.maxProgress;
    }

    private void craftItem(RecipeHolder<MechanicalTableRecipe> recipe) {
        ItemStack output = recipe.value().output();
        ItemStack mechanicalPliers = itemStackHandler.getStackInSlot(1);

        if (mechanicalPliers.getItem() instanceof MechanicalPliersItem) {
            mechanicalPliers.hurtAndBreak(1, (ServerLevel) this.level, null, item -> {});
        }

        if (itemStackHandler.extractItem(SLOT.BASE.ordinal(), 1, false).isEmpty()) return;
        for (int i = SLOT.INGREDIENT_1.ordinal(); i <= SLOT.INGREDIENT_4.ordinal(); i++) {
            if (itemStackHandler.extractItem(i, 1, false).isEmpty()) return;
        }

        ItemStack stackOutput = new ItemStack(output.getItem(), output.getCount());
        itemStackHandler.insertItem(SLOT.OUTPUT.ordinal(), stackOutput, false);
    }

    private Optional<RecipeHolder<MechanicalTableRecipe>> getCurrentRecipe() {
        List<RecipeHolder<MechanicalTableRecipe>> allRecipesForMechanicalTable = this.level.getRecipeManager().getAllRecipesFor(ModRecipe.MECHANICAL_TABLE_TYPE.get());
        List<ItemStack> listAdditions = Arrays.stream(new SLOT[]{SLOT.INGREDIENT_1, SLOT.INGREDIENT_2, SLOT.INGREDIENT_3, SLOT.INGREDIENT_4})
                .map(slot -> itemStackHandler.getStackInSlot(slot.ordinal()))
                .toList();

        return allRecipesForMechanicalTable.stream()
                .filter(rh -> rh.value().base().test(itemStackHandler.getStackInSlot(SLOT.BASE.ordinal())))
                .filter(rh -> rh.value().pliers().test(itemStackHandler.getStackInSlot(SLOT.PLIERS.ordinal())))
                .filter(rh -> listAdditions.stream()
                        .allMatch(itemStack -> rh.value().additions().test(itemStack))
                )
                .findFirst();
    }

}
