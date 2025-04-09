package com.mattutos.arkfuture.block.entity;

import com.mattutos.arkfuture.core.inventory.BaseData;
import com.mattutos.arkfuture.core.inventory.EnumContainerData;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipe;
import com.mattutos.arkfuture.crafting.recipe.mechanicaltable.MechanicalTableRecipeInput;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class MechanicalTableBlockEntity extends BlockEntity implements MenuProvider {

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
    private static final Logger log = LoggerFactory.getLogger(MechanicalTableBlockEntity.class);

    private final EnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyStorage);

    //LAZY ITEM HANDLER
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final EnumContainerData<DATA> data;

    //DATA FOR PROGRESS BAR
    private int progress = 0;
    private int maxProgress;

    //TODO - DEFINE MAX TABLE ENERGY CAPACITY BASED ON THE TABLE TIER
    public static final int CAPACITY = 5_000;

    private @NotNull EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY);
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(7) { //5 TO CRAFT +1 TO MECHANICAL PLIERS +1 TO OUTPUT
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 64;
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

        data = new EnumContainerData<>(DATA.class) {
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


    private void resetProgress() {
        this.progress = 0;
    }


    //AVOID UNNECESSARY LOADS
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    //ENSURE THAT OLD CRAFTING WON'T BE USED
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }


    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("mechanical_table.progress", progress);
        pTag.putInt("mechanical_table.max_progress", maxProgress);
        pTag.put("mechanical_table.energy_stored", energyStorage.serializeNBT(pRegistries));
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("mechanical_table.progress");
        maxProgress = pTag.getInt("mechanical_table.max_progress");
        energyStorage.deserializeNBT(pRegistries, pTag.get("mechanical_table.energy_stored"));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ark_future.mechanical_table");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MechanicalTableMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }


    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
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
                setChanged(level, blockPos, blockState);
            }
        }
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    private void craftItem() {
        Optional<RecipeHolder<MechanicalTableRecipe>> recipe = getCurrentRecipe();

        if (recipe.isPresent()) {
            ItemStack output = recipe.get().value().getOutput();


            //HERE IS FIVE (5) CAUSE IT JUST BEING CONSIDERED THE 5 PRINCIPAL SLOTS TO CRAFT
            for (int i = 0; i < 5; i++) {
                if (itemHandler.extractItem(i, 1, false).isEmpty()) return;
            }

            ItemStack stackOutput = new ItemStack(output.getItem(), output.getCount());
            itemHandler.insertItem(OUTPUT_SLOT, stackOutput, false);
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
            ItemStack inputSlotStack = itemHandler.getStackInSlot(i);

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
        MechanicalTableRecipeInput recipeInput = new MechanicalTableRecipeInput(itemHandler);

        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipe.MECHANICAL_TABLE_TYPE.get(), recipeInput, level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

}
