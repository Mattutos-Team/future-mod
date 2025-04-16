package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArkFuture.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARK_FUTURE_TAB = CREATIVE_MODE_TAB.register("ark_future_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BlockInit.COAL_POWER_GENERATOR.get()))
                    .title(Component.translatable("creativetab.ark_future.ark_future_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemInit.ANCIENT_ORE_ITEM.get());
                        output.accept(BlockInit.ANCIENT_ORE_BLOCK_ITEM.get());
                        output.accept(BlockInit.ANCIENT_ORE_INGOT_BLOCK_ITEM.get());
                        output.accept(ItemInit.ANCIENT_ORE_INGOT_ITEM.get());
                        output.accept(BlockInit.ANCIENT_ORE_VEIN_BLOCK_ITEM.get());
                        output.accept(BlockInit.DEEPSLATE_ANCIENT_ORE_VEIN_BLOCK_ITEM.get());

                        output.accept(BlockInit.COAL_POWER_GENERATOR.get());
                        output.accept(BlockInit.MECHANICAL_TABLE.get());

                        output.accept(ItemInit.BATTERY_10K.get());

                        output.accept(ItemInit.GOLDEN_THREAD.get());
                        output.accept(ItemInit.SIMPLE_ENERGIZED_ANCIENT_IRON.get());
                        output.accept(ItemInit.MECHANICAL_PLIERS.get());
                        output.accept(ItemInit.ANCIENT_HAMMER.get());
                        output.accept(ItemInit.ANCIENT_PLATE.get());

                        output.accept(ItemInit.ENERGY_PISTOL.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}