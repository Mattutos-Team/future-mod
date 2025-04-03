package com.mattutos.arkfuture.item;

import com.mattutos.arkfuture.init.DataComponentTypesInit;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatteryItem extends Item {

    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;

    public BatteryItem(Properties pProperties, int capacity, int maxReceive, int maxExtract) {
        super(pProperties.stacksTo(1));
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public @NotNull ICapabilityProvider getCapabilityProvider(ItemStack stack) {
        return new BatteryCapability(stack, capacity, maxReceive, maxExtract);
    }

    public static int getEnergy(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypesInit.ENERGY.get(), 0).intValue();
    }

    public static void setEnergy(ItemStack stack, long energy) {
        stack.set(DataComponentTypesInit.ENERGY.get(), energy);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        int energy = getEnergy(pStack);
        return Math.round((energy / (float) capacity) * 13); // A barra no MC tem 13 pixels de largura
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return Mth.hsvToRgb((float) getEnergy(pStack) / capacity * 0.33F, 1.0F, 1.0F);
    }

    private static class BatteryCapability implements ICapabilityProvider, IEnergyStorage {
        private final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(() -> this);
        private final ItemStack stack;
        private final int capacity;
        private final int maxReceive;
        private final int maxExtract;

        public BatteryCapability(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
            this.stack = stack;
            this.capacity = capacity;
            this.maxReceive = maxReceive;
            this.maxExtract = maxExtract;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) return 0;

            int stored = BatteryItem.getEnergy(stack);
            int energyReceived = Math.min(capacity - stored, Math.min(this.maxReceive, maxReceive));

            if (!simulate) BatteryItem.setEnergy(stack, stored + energyReceived);

            return energyReceived;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract()) return 0;

            int stored = BatteryItem.getEnergy(stack);
            int energyExtracted = Math.min(stored, Math.min(this.maxExtract, maxExtract));

            if (!simulate) BatteryItem.setEnergy(stack, stored - energyExtracted);

            return energyExtracted;
        }

        @Override
        public int getEnergyStored() {
            return BatteryItem.getEnergy(stack);
        }

        @Override
        public int getMaxEnergyStored() {
            return capacity;
        }

        @Override
        public boolean canExtract() {
            return this.maxExtract > 0;
        }

        @Override
        public boolean canReceive() {
            return this.maxReceive > 0;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ForgeCapabilities.ENERGY.orEmpty(cap, energyStorage);
        }
    }
}
