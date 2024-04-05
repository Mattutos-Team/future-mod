package com.mattutos.future;

import com.mattutos.future.init.BlockInit;
import com.mattutos.future.init.CreativeModTabInit;
import com.mattutos.future.init.ItemInit;
import com.mattutos.future.init.RecipeSerializerInit;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Slf4j
@Mod(FutureMod.MOD_ID)
public class FutureMod {
    public static final String MOD_ID = "future_mod";

    public FutureMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CreativeModTabInit.register(modEventBus);
        ItemInit.register(modEventBus);
        BlockInit.register(modEventBus);
        RecipeSerializerInit.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ItemInit.WOOD_CIRCUIT);
            event.accept(ItemInit.RAW_OLD_ORE);
        }
    }

}
