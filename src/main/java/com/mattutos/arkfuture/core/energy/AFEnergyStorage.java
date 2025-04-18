package com.mattutos.arkfuture.core.energy;

import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class AFEnergyStorage extends EnergyStorage {
    public AFEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public AFEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setChanged() {
    }

    public void setEnergy(int amount) {
        this.energy = amount;
        setChanged();
    }

    public void setCapacity(int amount) {
        this.capacity = amount;
        setChanged();
    }

    public void setMaxReceive(int amount) {
        this.maxReceive = amount;
        setChanged();
    }

    public void setMaxExtract(int amount) {
        this.maxExtract = amount;
        setChanged();
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractEnergy = super.extractEnergy(maxExtract, simulate);
        setChanged();
        return extractEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        setChanged();
        return receiveEnergy;
    }

    public int extractMaxEnergy(boolean simulate) {
        int energyExtracted = Math.min(this.energy, this.maxExtract);
        if (!simulate) {
            this.energy -= energyExtracted;
            setChanged();
        }
        return energyExtracted;
    }

    public int receiveMaxEnergy(boolean simulate) {
        int energyReceived = Math.min(this.capacity - this.energy, this.maxReceive);
        if (!simulate) {
            this.energy += energyReceived;
            setChanged();
        }
        return energyReceived;
    }

    public int forceExtractEnergy(int amount, boolean simulate) {
        int energyExtracted = Math.min(this.energy, amount);
        if (!simulate) {
            this.energy -= energyExtracted;
            setChanged();
        }
        return energyExtracted;
    }

    public int forceReceiveEnergy(int amount, boolean simulate) {
        int energyReceived = Math.min(this.capacity - this.energy, amount);
        if (!simulate) {
            this.energy += energyReceived;
            setChanged();
        }
        return energyReceived;
    }

    public void sendMaxEnergyTo(IEnergyStorage target) {
        this.sendEnergyTo(target, this.energy);
    }

    public void sendEnergyTo(IEnergyStorage target, int amount) {
        int energyExtracted = this.extractEnergy(amount, true);
        int energyReceived = target.receiveEnergy(energyExtracted, false);
        this.extractEnergy(energyReceived, false);
    }

    public void receiveMaxEnergyFrom(IEnergyStorage source) {
        this.receiveEnergyFrom(source, this.capacity - this.energy);
    }

    public void receiveEnergyFrom(IEnergyStorage source, int amount) {
        int energyExtracted = source.extractEnergy(amount, true);
        int energyReceived = this.receiveEnergy(energyExtracted, false);
        source.extractEnergy(energyReceived, false);
    }
}
