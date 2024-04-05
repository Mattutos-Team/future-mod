package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FutureMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FUTURE_MOD_TAB =
            CREATIVE_MODE_TAB.register("future_mod_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemInit.WOOD_CIRCUIT.get()))
                    .title(Component.translatable("creative.future_mod_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {

                        //ITEMS
                        output.accept(ItemInit.WOOD_CIRCUIT.get());
                        output.accept(ItemInit.COOPER_CIRCUIT.get());
                        output.accept(ItemInit.REDSTONE_CHIP.get());
                        output.accept(ItemInit.VARNISH.get());
                        output.accept(ItemInit.VARNISHED_WOOD.get());

                        //RAW
                        output.accept(ItemInit.RAW_OLD_ORE.get());

                        //ALLOY
                        output.accept(ItemInit.OLD_ALLOY.get());

                        //INGOT
                        output.accept(ItemInit.OLD_INGOT.get());

                        //FUEL

                        //FOODS

                        //BLOCKS
                        output.accept(BlockInit.OLD_ORE.get());
                        output.accept(BlockInit.COAL_ENERGY_GENERATOR.get());
                    }))
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}