package com.mattutos.arkfuture.screen.util;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FuelSlot extends Slot {
    public FuelSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return mayPlaceItem(pStack);
    }

    public static boolean mayPlaceItem(ItemStack pItemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pItemStack, null) > 0;
    }
}
