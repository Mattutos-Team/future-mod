package com.mattutos.arkfuture.screen.util;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergySlot extends Slot {
    public EnergySlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return mayPlaceItem(pStack);
    }

    public static boolean mayPlaceItem(ItemStack pItemStack) {
        // TODO: identificar itens que contem energia
        return pItemStack.getItem() instanceof IEnergyStorage;
    }
}