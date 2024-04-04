package com.mattutos.future.item;

import com.mattutos.future.FutureMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FutureMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FUTURE_MOD_TAB = CREATIVE_MODE_TAB.register("future_mod_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.WOOD_CIRCUIT.get())).title(Component.translatable("creative.future_mod_tab"))
            .displayItems(((itemDisplayParameters, output) -> {

                //ITEMS
                output.accept(ModItems.WOOD_CIRCUIT.get());
                output.accept(ModItems.COOPER_CIRCUIT.get());
                output.accept(ModItems.REDSTONE_CHIP.get());
                output.accept(ModItems.VARNISH.get());
                output.accept(ModItems.VARNISHED_WOOD.get());

                //FUEL

                //FOODS

                //BLOCKS
            }))
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}