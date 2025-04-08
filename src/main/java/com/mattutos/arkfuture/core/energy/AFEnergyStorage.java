package com.mattutos.arkfuture.core.energy;

import net.minecraftforge.energy.EnergyStorage;

public class AFEnergyStorage extends EnergyStorage {
    public AFEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int amount) {
        this.energy = amount;
    }

    public int forceExtractEnergy(int amount, boolean simulate) {
        int energyExtracted = Math.min(this.energy, amount);
        if (!simulate)
            this.energy -= energyExtracted;
        return energyExtracted;
    }

    public int forceReceiveEnergy(int amount, boolean simulate) {
        int energyReceived = Math.min(this.capacity - this.energy, amount);
        if (!simulate)
            this.energy += energyReceived;
        return energyReceived;
    }
}
