package com.mattutos.arkfuture.config;

import com.mattutos.arkfuture.block.client.vitalenergycube.VitalEnergyCubeRenderer;
import com.mattutos.arkfuture.entity.client.EnergyProjectileRenderer;
import com.mattutos.arkfuture.init.BlockEntityInit;
import com.mattutos.arkfuture.init.EntityInit;
import com.mattutos.arkfuture.init.MenuInit;
import com.mattutos.arkfuture.item.common.SingleItemTooltip;
import com.mattutos.arkfuture.item.common.SingleItemTooltipComponent;
import com.mattutos.arkfuture.screen.CoalPowerGeneratorScreen;
import com.mattutos.arkfuture.screen.MechanicalTableScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
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
            MenuScreens.register(MenuInit.MECHANICAL_TABLE_MENU.get(), MechanicalTableScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ENERGY_PROJECTILE.get(), EnergyProjectileRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.VITAL_ENERGY_CUBE.get(), ctx -> new VitalEnergyCubeRenderer(null));
    }

    @SubscribeEvent
    public static void registerTooltipFactory(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SingleItemTooltip.class, tooltip -> new SingleItemTooltipComponent(tooltip.itemStack(), tooltip.text()));
    }
}
