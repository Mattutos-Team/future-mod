package com.mattutos.arkfuture.config;

import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.screen.CoalPowerGeneratorScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.mattutos.arkfuture.ArkFuture.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuInit.COAL_POWER_GENERATOR_MENU.get(), CoalPowerGeneratorScreen::new);
        });
    }

}
