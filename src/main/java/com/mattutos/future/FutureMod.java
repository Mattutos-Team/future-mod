package com.mattutos.future;

import com.mattutos.future.init.BlockEntityInit;
import com.mattutos.future.init.BlockInit;
import com.mattutos.future.init.CreativeModTabInit;
import com.mattutos.future.init.ItemInit;
import com.mattutos.future.init.MenuInit;
import com.mattutos.future.init.RecipeSerializerInit;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

@Mod(FutureMod.MOD_ID)
public class FutureMod {
    public static final String MOD_ID = "future_mod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public FutureMod(FMLJavaModLoadingContext context) {
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
