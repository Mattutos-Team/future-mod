package com.mattutos.ark_future.init;

import com.mattutos.ark_future.ArkFuture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArkFuture.MOD_ID);

    public static final RegistryObject<Item> ANCIENT_ORE_ITEM = ITEMS.register(
            "ancient_ore",
            () -> new Item(
                    new Item.Properties().useItemDescriptionPrefix().setId(
                            ResourceKey.create(
                                    Registries.ITEM,
                                    ResourceLocation.parse(
                                            String.format("%s:%s", ArkFuture.MOD_ID, "ancient_ore")
                                    )
                            )
                    )
            )
    );

    public static final RegistryObject<Item> ANCIENT_ORE_BLOCK_ITEM = ITEMS.register(
            "ancient_ore_block",
            () -> new Item(
                    new Item.Properties().useItemDescriptionPrefix().setId(
                            ResourceKey.create(
                                    Registries.ITEM, ResourceLocation.parse(
                                            String.format("%s:%s", ArkFuture.MOD_ID, "ancient_ore_block")
                                    )
                            )
                    )
            )
    );

    public static final RegistryObject<Item> ANCIENT_ORE_INGOT_ITEM = ITEMS.register(
            "ancient_ore_ingot",
        () -> new Item(
                new Item.Properties().useItemDescriptionPrefix().setId(
                        ResourceKey.create(
                                Registries.ITEM, ResourceLocation.parse(
                                        String.format("%s:%s", ArkFuture.MOD_ID, "ancient_ore_ingot")
                                )
                        )
                )
        )
    );

    public static final RegistryObject<Item> ANCIENT_ORE_VEIN_BLOCK_ITEM = ITEMS.register(
            "ancient_ore_vein_block",
            () -> new Item(
                    new Item.Properties().useItemDescriptionPrefix().setId(
                            ResourceKey.create(
                                    Registries.ITEM, ResourceLocation.parse(
                                            String.format("%s:%s", ArkFuture.MOD_ID, "ancient_ore_vein_block")
                                    )
                            )
                    )
            )
    );

    public static final RegistryObject<Item> DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM = ITEMS.register(
            "deepslate_ancient_ore_vein_block",
            () -> new Item(
                    new Item.Properties().useItemDescriptionPrefix().setId(
                            ResourceKey.create(
                                    Registries.ITEM, ResourceLocation.parse(
                                            String.format("%s:%s", ArkFuture.MOD_ID, "deepslate_ancient_ore_vein_block")
                                    )
                            )
                    )
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
