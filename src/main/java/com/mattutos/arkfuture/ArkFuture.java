package com.mattutos.arkfuture;

import com.mattutos.arkfuture.init.*;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Slf4j
@Mod(ArkFuture.MOD_ID)
public class ArkFuture {
    public static final String MOD_ID = "ark_future";

    public ArkFuture(FMLJavaModLoadingContext context) {
        log.info("Carregando mod");
        erro de compilação

        IEventBus modEventBus = context.getModEventBus();

        BlockEntityInit.register(modEventBus);
        BlockInit.register(modEventBus);
        CreativeModTabInit.register(modEventBus);
        ItemInit.register(modEventBus);
        ModRecipe.register(modEventBus);
        MenuInit.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

}
