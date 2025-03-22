package com.mattutos.ark_future;

import com.mattutos.ark_future.init.BlockEntityInit;
import com.mattutos.ark_future.init.BlockInit;
import com.mattutos.ark_future.init.CreativeModTabInit;
import com.mattutos.ark_future.init.ItemInit;
import com.mattutos.ark_future.init.MenuInit;
import com.mattutos.ark_future.init.RecipeSerializerInit;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

@Mod(ArkFuture.MOD_ID)
public class ArkFuture {
    public static final String MOD_ID = "ark_future";

    private static final Logger LOGGER = LogUtils.getLogger();

    public ArkFuture(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        BlockEntityInit.register(modEventBus);
        BlockInit.register(modEventBus);
        CreativeModTabInit.register(modEventBus);
        ItemInit.register(modEventBus);
        MenuInit.register(modEventBus);
        RecipeSerializerInit.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // TODO: ...
        }
    }
}
