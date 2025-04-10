package com.mattutos.arkfuture.item.common;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record SingleItemTooltip(ItemStack itemStack, String text) implements TooltipComponent {
}
