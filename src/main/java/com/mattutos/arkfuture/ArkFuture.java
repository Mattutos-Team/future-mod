package com.mattutos.arkfuture;

import com.mattutos.arkfuture.event.ExplosionHandler;
import com.mattutos.arkfuture.init.*;
import com.mattutos.arkfuture.init.recipe.ModRecipe;
import com.mattutos.arkfuture.networking.AFPacketHandler;
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

        IEventBus modEventBus = context.getModEventBus();

        AFPacketHandler.register();
        BlockEntityInit.register(modEventBus);
        BlockInit.register(modEventBus);
        CreativeModTabInit.register(modEventBus);
        DataComponentTypesInit.register(modEventBus);
        EntityInit.register(modEventBus);
        ItemInit.register(modEventBus);
        ModRecipe.register(modEventBus);
        MenuInit.register(modEventBus);

        // ✅ Register your event handler here
        MinecraftForge.EVENT_BUS.register(ExplosionHandler.class);

        MinecraftForge.EVENT_BUS.register(this);
    }

}
