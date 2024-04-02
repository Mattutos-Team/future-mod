package com.mattutos.future.item;

import com.mattutos.future.FutureMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FutureMod.MODID);

    //CIRCUITS
    public static final RegistryObject<Item> WOOD_CIRCUIT = ITEMS.register("wood_circuit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COOPER_CIRCUIT = ITEMS.register("cooper_circuit", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
