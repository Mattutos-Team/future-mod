package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.screen.CoalPowerGeneratorMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArkFuture.MOD_ID);

    public static final RegistryObject<MenuType<CoalPowerGeneratorMenu>> COAL_POWER_GENERATOR_MENU =
            MENU_TYPE.register("coal_power_generator_menu", () -> IForgeMenuType.create(CoalPowerGeneratorMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPE.register(eventBus);
    }

}
