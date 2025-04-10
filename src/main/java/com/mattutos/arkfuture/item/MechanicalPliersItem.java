package com.mattutos.arkfuture.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MechanicalPliersItem extends Item {

    public MechanicalPliersItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setDamageValue(itemStack.getDamageValue() + 4);
        if (result.getDamageValue() >= result.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return result;
    }
}
