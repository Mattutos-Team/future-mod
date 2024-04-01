package com.mattutos.future.item.custom;

import com.mattutos.future.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class WoodCircuitItem {
    public static  final RegistryObject<Item> FUTURE_INGOT = ModItems.ITEMS.register("future_ingot", ()-> new Item(new Item.Properties()));

}
