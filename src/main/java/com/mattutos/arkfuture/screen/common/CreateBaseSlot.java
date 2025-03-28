package com.mattutos.arkfuture.screen.common;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CreateBaseSlot extends SlotItemHandler {


    public CreateBaseSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return super.mayPlace(stack);
    }

    public static boolean mayPlaceItem(ItemStack pItemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pItemStack, null) > 0;
    }
}
