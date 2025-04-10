package com.mattutos.arkfuture.item.util;

import com.mattutos.arkfuture.core.energy.AFEnergyStorage;
import com.mattutos.arkfuture.init.DataComponentTypesInit;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyCapability implements ICapabilityProvider {

    private final LazyOptional<IEnergyStorage> lazyEnergyStorage;
    private final ItemStack stack;

    public ItemEnergyCapability(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        this.stack = stack;
        int energy = getEnergy(stack);

        // Define o armazenamento com delegação da lógica
        this.lazyEnergyStorage = LazyOptional.of(() -> createEnergyStorage(capacity, maxReceive, maxExtract, energy));
    }

    private IEnergyStorage createEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        return new AFEnergyStorage(capacity, maxReceive, maxExtract, energy) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (!simulate) setDataComponentEnergy(this.getEnergyStored());
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int extracted = super.extractEnergy(maxExtract, simulate);
                if (!simulate) setDataComponentEnergy(this.getEnergyStored());
                return extracted;
            }

            @Override
            public int forceExtractEnergy(int amount, boolean simulate) {
                int extracted = super.forceExtractEnergy(amount, simulate);
                if (!simulate) setDataComponentEnergy(this.getEnergyStored());
                return extracted;
            }

            @Override
            public int forceReceiveEnergy(int amount, boolean simulate) {
                int received = super.forceReceiveEnergy(amount, simulate);
                if (!simulate) setDataComponentEnergy(this.getEnergyStored());
                return received;
            }
        };
    }

    public static int getEnergy(ItemStack pStack) {
        return pStack.getOrDefault(DataComponentTypesInit.ENERGY.get(), 0).intValue();
    }

    private void setDataComponentEnergy(int energy) {
        stack.set(DataComponentTypesInit.ENERGY.get(), (long) energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(cap, lazyEnergyStorage);
    }
}