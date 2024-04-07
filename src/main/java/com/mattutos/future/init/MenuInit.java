package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import com.mattutos.future.menu.CoalGeneratorContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FutureMod.MOD_ID);

    public static final RegistryObject<MenuType<CoalGeneratorContainerMenu>> COAL_ENERGY_GENERATOR_MENU = MENU_TYPE.register("coal_energy_generator_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) -> new CoalGeneratorContainerMenu(windowId, inv.player, data.readBlockPos())));


    public static void register(IEventBus eventBus) {
        MENU_TYPE.register(eventBus);
    }

}
