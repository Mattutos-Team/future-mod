package com.mattutos.future.blockentity;

import com.mattutos.future.FutureMod;
import com.mattutos.future.blockentity.util.CustomEnergyStorage;
import com.mattutos.future.blockentity.util.TickableBlockEntity;
import com.mattutos.future.init.BlockEntityInit;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyCoalGeneratorEntity extends BlockEntity implements TickableBlockEntity {
    private static final Component TITLE =
            Component.translatable("container." + FutureMod.MOD_ID + ".coal_energy_generator");

    @Getter
    private final ItemStackHandler inventory = new ItemStackHandler(1);
    @Getter
    private final LazyOptional<ItemStackHandler> inventoryOptional = LazyOptional.of(() -> this.inventory);

    @Getter
    private final CustomEnergyStorage energy = new CustomEnergyStorage(10000, 0, 100, 0);
    @Getter
    private final LazyOptional<CustomEnergyStorage> energyOptional = LazyOptional.of(() -> this.energy);

    private int burnTime = 0, maxBurnTime = 0;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> EnergyCoalGeneratorEntity.this.energy.getEnergyStored();
                case 1 -> EnergyCoalGeneratorEntity.this.energy.getMaxEnergyStored();
                case 2 -> EnergyCoalGeneratorEntity.this.burnTime;
                case 3 -> EnergyCoalGeneratorEntity.this.maxBurnTime;
                default -> throw new UnsupportedOperationException("Unexpected value: " + pIndex);
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> EnergyCoalGeneratorEntity.this.energy.setEnergy(pValue);
                case 2 -> EnergyCoalGeneratorEntity.this.burnTime = pValue;
                case 3 -> EnergyCoalGeneratorEntity.this.maxBurnTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public EnergyCoalGeneratorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.ENERGY_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;

        if(this.energy.getEnergyStored() < this.energy.getMaxEnergyStored()) {
            if(this.burnTime <= 0) {
                if(canBurn(this.inventory.getStackInSlot(0))) {
                    this.burnTime = this.maxBurnTime = getBurnTime(this.inventory.getStackInSlot(0));
                    this.inventory.getStackInSlot(0).shrink(1);
                    sendUpdate();
                }
            } else {
                this.burnTime--;
                this.energy.addEnergy(1);
                sendUpdate();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);

        var tutorialmodData = new CompoundTag();
        tutorialmodData.put("Inventory", this.inventory.serializeNBT());
        tutorialmodData.put("Energy", this.energy.serializeNBT());
        tutorialmodData.putInt("BurnTime", this.burnTime);
        tutorialmodData.putInt("MaxBurnTime", this.maxBurnTime);
        nbt.put(FutureMod.MOD_ID, tutorialmodData);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        CompoundTag tutorialmodData = nbt.getCompound(FutureMod.MOD_ID);
        if(tutorialmodData.isEmpty())
            return;

        if (tutorialmodData.contains("Inventory", Tag.TAG_COMPOUND)) {
            this.inventory.deserializeNBT(tutorialmodData.getCompound("Inventory"));
        }

        if(tutorialmodData.contains("Energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(tutorialmodData.get("Energy"));
        }

        if (tutorialmodData.contains("BurnTime", Tag.TAG_INT)) {
            this.burnTime = tutorialmodData.getInt("BurnTime");
        }

        if (tutorialmodData.contains("MaxBurnTime", Tag.TAG_INT)) {
            this.maxBurnTime = tutorialmodData.getInt("MaxBurnTime");
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryOptional.cast();
        } else if (cap == ForgeCapabilities.ENERGY) {
            return this.energyOptional.cast();
        } else {
            return super.getCapability(cap);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryOptional.invalidate();
        this.energyOptional.invalidate();
    }

//    @Override
//    public @NotNull Component getDisplayName() {
//        return TITLE;
//    }

//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
//        return new ExampleEnergyGeneratorMenu(pContainerId, pPlayerInventory, this, this.containerData);
//    }

    private void sendUpdate() {
        setChanged();

        if(this.level != null)
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
    }

    public boolean canBurn(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }
}
