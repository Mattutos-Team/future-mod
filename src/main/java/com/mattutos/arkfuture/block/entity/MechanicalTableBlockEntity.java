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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MechanicalTableBlockEntity extends BlockEntity implements MenuProvider {
    private static final int INPUT_SLOT_1 = 0;  // First input slot
    private static final int INPUT_SLOT_2 = 1;  // Second input slot
    private static final int INPUT_SLOT_3 = 2;  // Third input slot
    private static final int INPUT_SLOT_4 = 3;  // Fourth input slot
    private static final int INPUT_SLOT_5 = 4;  // Fifth input slot
    private static final int OUTPUT_SLOT = 5;   // Output slot
    private static final Logger log = LoggerFactory.getLogger(MechanicalTableBlockEntity.class);

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public final ItemStackHandler itemHandler = new ItemStackHandler(6) { //5 TO CRAFT +1 TO OUTPUT
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
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> MechanicalTableBlockEntity.this.progress;
                    case 1 -> MechanicalTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0:
                        MechanicalTableBlockEntity.this.progress = value;
                    case 1:
                        MechanicalTableBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }


    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
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

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("mechanical_table.progress");
        maxProgress = pTag.getInt("mechanical_table.max_progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mechanical Table");
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
            ItemStack output = recipe.get().value().getOutput();

            for (int i = 0; i < 5; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                    itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
        }


    }


    private boolean hasRecipe() {
        Optional<RecipeHolder<MechanicalTableRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        MechanicalTableRecipe currentRecipe = recipe.get().value();
        boolean canCraft = true;

        for (int i = 0; i < 5; i++) {
            ItemStack inputSlotStack = itemHandler.getStackInSlot(i);

            if (!currentRecipe.isBaseIngredient(inputSlotStack) && !currentRecipe.isAdditionIngredient(inputSlotStack)) {
                canCraft = false;
                break;
            }
        }

        ItemStack output = currentRecipe.getOutput();
        return canCraft && canInsertItemIntoOutputSlot(output) && canInsertAmountIntoOutputSlot(output.getCount());
    }

    private Optional<RecipeHolder<MechanicalTableRecipe>> getCurrentRecipe() {
        List<ItemStack> inputs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }


        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipe.MECHANICAL_TABLE_TYPE.get(), new MechanicalTableRecipeInput(inputs), level);
    }


    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
