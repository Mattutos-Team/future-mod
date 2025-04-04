package com.mattutos.arkfuture.item;

import com.mattutos.arkfuture.init.DataComponentTypesInit;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

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
        return new ItemEnergyCapability(stack, capacity, maxReceive, maxExtract);
    }

    public int getEnergy(ItemStack pStack) {
        return pStack.getOrDefault(DataComponentTypesInit.ENERGY.get(), 0).intValue();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        int energy = getEnergy(pStack);
        return Math.round((energy / (float) capacity) * 13); // A barra no MC tem 13 pixels de largura
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        float percentage = getEnergy(pStack) / (float) capacity;
        return Mth.hsvToRgb(percentage * 0.33F, 1.0F, 1.0F);
    }

}
