package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.BatteryItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArkFuture.MOD_ID);

    public static final RegistryObject<Item> ANCIENT_ORE_ITEM = ITEMS.register("ancient_ore", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ORE_INGOT_ITEM = ITEMS.register("ancient_ore_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTERY_10K = ITEMS.register("battery_10k", () -> new BatteryItem(new Item.Properties(), 10_000, 100, 100));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
