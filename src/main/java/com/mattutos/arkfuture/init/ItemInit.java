package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.BatteryItem;
import com.mattutos.arkfuture.item.EnergyPistolItem;
import com.mattutos.arkfuture.item.GoldenThreadItem;
import com.mattutos.arkfuture.item.MechanicalPliersItem;
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

    public static final RegistryObject<Item> MECHANICAL_PLIERS = ITEMS.register("mechanical_pliers", () -> new MechanicalPliersItem(new Item.Properties().durability(50)));

    public static final RegistryObject<Item> GOLDEN_THREAD = ITEMS.register("golden_thread", () -> new GoldenThreadItem(new Item.Properties()));

    public static final RegistryObject<Item> ENERGY_PISTOL = ITEMS.register("energy_pistol", () -> new EnergyPistolItem(new Item.Properties()));
    public static final RegistryObject<Item> ENERGY_PROJECTILE = ITEMS.register("energy_projectile", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
