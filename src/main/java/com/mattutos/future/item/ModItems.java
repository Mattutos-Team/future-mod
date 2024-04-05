package com.mattutos.future.item;

import com.mattutos.future.FutureMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FutureMod.MOD_ID);

    //CIRCUITS
    public static final RegistryObject<Item> WOOD_CIRCUIT = ITEMS.register("wood_circuit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COOPER_CIRCUIT = ITEMS.register("cooper_circuit", () -> new Item(new Item.Properties()));

    //REDSTONE CHIP
    public static final RegistryObject<Item> REDSTONE_CHIP = ITEMS.register("redstone_chip", () -> new Item(new Item.Properties()));

    //VARNISH
    public static final RegistryObject<Item> VARNISH = ITEMS.register("varnish", () -> new Item(new Item.Properties().durability(8)));
    public static final RegistryObject<Item> VARNISHED_WOOD = ITEMS.register("varnished_wood", () -> new Item(new Item.Properties()));

    //RAW
    public static final RegistryObject<Item> RAW_OLD_ORE = ITEMS.register("raw_old_ore", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
