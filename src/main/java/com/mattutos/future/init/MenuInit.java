package com.mattutos.future.init;

import com.mattutos.future.FutureMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FutureMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        MENU_TYPE.register(eventBus);
    }

}
