package com.mattutos.arkfuture.screen.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FuelSlot extends SlotItemHandler {
    public FuelSlot(IItemHandler pItemHandler, int pSlot, int pX, int pY) {
        super(pItemHandler, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return mayPlaceItem(pStack);
    }

    public static boolean mayPlaceItem(ItemStack pItemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pItemStack, null) > 0;
    }
}
