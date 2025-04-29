package com.mattutos.arkfuture.item.common;

import com.mattutos.arkfuture.item.BatteryItem;
import com.mattutos.arkfuture.item.util.ItemEnergyCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AFEnergyBar extends BlockItem {

    private final int capacity;


    public AFEnergyBar(Block pBlock, Properties pProperties, int capacity) {
        super(pBlock, pProperties);
        this.capacity = capacity;
    }

    @Override
    public @NotNull ICapabilityProvider getCapabilityProvider(ItemStack stack) {
        return new ItemEnergyCapability(stack, capacity, capacity, capacity);
    }


    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        int energy = ItemEnergyCapability.getEnergy(pStack);
        return Math.round((energy / (float) capacity) * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        float percentage = ItemEnergyCapability.getEnergy(pStack) / (float) capacity;
        return Mth.hsvToRgb(percentage * 0.33F, 1.0F, 1.0F);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        if (pStack.getItem() instanceof BatteryItem batteryItem) {
            int energy = ItemEnergyCapability.getEnergy(pStack);
            int capacity = batteryItem.getCapacity();

            pTooltipComponents.add(Component.translatable("tooltip.ark_future.item.battery.energy", energy, capacity).withStyle(ChatFormatting.DARK_GREEN));
        }
    }
}
