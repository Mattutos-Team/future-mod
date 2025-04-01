package com.mattutos.arkfuture.menu.common;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class EnergySlot extends SlotItemHandler {
    public EnergySlot(IItemHandler pItemHandler, int pSlot, int pX, int pY) {
        super(pItemHandler, pSlot, pX, pY);
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