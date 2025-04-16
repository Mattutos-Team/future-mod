package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.BatteryItem;
import com.mattutos.arkfuture.item.EnergyPistolItem;
import com.mattutos.arkfuture.item.common.AFRemainingItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArkFuture.MOD_ID);

    public static final RegistryObject<Item> ANCIENT_ORE_ITEM = ITEMS.register("ancient_ore", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_ORE_INGOT_ITEM = ITEMS.register("ancient_ore_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BATTERY_10K = ITEMS.register("battery_10k", () -> new BatteryItem(new Item.Properties(), 10_000, 100));

    public static final RegistryObject<Item> MECHANICAL_PLIERS = ITEMS.register("mechanical_pliers", () -> new AFRemainingItem(new Item.Properties().durability(50),4 ));
    public static final RegistryObject<Item> ANCIENT_HAMMER = ITEMS.register("ancient_hammer", () -> new AFRemainingItem(new Item.Properties().durability(20)));
    public static final RegistryObject<Item> ANCIENT_PLATE = ITEMS.register("ancient_plate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BASIC_PROCESSOR = ITEMS.register("basic_processor", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> GOLDEN_THREAD = ITEMS.register("golden_thread", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SIMPLE_ENERGIZED_ANCIENT_IRON = ITEMS.register("simple_energized_ancient_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ENERGY_PISTOL = ITEMS.register("energy_pistol", () -> new EnergyPistolItem(new Item.Properties()));
    public static final RegistryObject<Item> ENERGY_PROJECTILE = ITEMS.register("energy_projectile", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
