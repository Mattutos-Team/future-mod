package com.mattutos.arkfuture.block.entity.util;

import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AFEnergyContainerBlockEntity extends AFBaseContainerBlockEntity {

    public static final String ENERGY_KEY = "Energy";
    protected LazyOptional<AFEnergyStorage> lazyEnergyStorage;

    protected AFEnergyContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected abstract AFEnergyStorage getEnergyStorage();

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyStorage.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        getEnergyStorage().deserializeNBT(pRegistries, pTag.get(ENERGY_KEY));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put(ENERGY_KEY, getEnergyStorage().serializeNBT(pRegistries));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (ForgeCapabilities.ENERGY == cap && !this.remove) {
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    protected @NotNull AFEnergyStorage createEnergyStorage(int capacity) {
        return createEnergyStorage(capacity, capacity);
    }

    protected @NotNull AFEnergyStorage createEnergyStorage(int capacity, int maxTransfer) {
        return createEnergyStorage(capacity, maxTransfer, maxTransfer, 0);
    }

    protected @NotNull AFEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        return new AFEnergyStorage(capacity, maxReceive, maxExtract, energy) {
            @Override
            public void setChanged() {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }
}
