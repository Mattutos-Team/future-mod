package com.mattutos.arkfuture.item.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AFRemainingItem extends Item {

    int damage;

    public AFRemainingItem(Properties pProperties) {
        this(pProperties, 1);
    }

    public AFRemainingItem(Properties pProperties, int damage) {
        super(pProperties);
        this.damage = damage;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setDamageValue(itemStack.getDamageValue() + this.damage);
        if (result.getDamageValue() >= result.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return result;
    }

}
