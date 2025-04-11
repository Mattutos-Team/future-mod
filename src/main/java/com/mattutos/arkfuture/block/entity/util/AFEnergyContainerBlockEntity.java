package com.mattutos.arkfuture.block.entity.util;

import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AFEnergyContainerBlockEntity extends AFBaseContainerBlockEntity {

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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (ForgeCapabilities.ENERGY == cap) {
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }
}
