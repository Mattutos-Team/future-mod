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
                        output.accept(BlockInit.COAL_POWER_GENERATOR.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}