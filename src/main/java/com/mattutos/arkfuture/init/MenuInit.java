package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.menu.MechanicalTableMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArkFuture.MOD_ID);

    @OnlyIn(Dist.CLIENT)

    public static void onClientSetup() {
        MenuScreens.register(mechanical_table.get(), SculkCraftScreen::new);
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> menu(String name, Supplier<? extends MenuType<T>> container) {
        return MENUS.register(name, container);
    }

    public static RegistryObject<MenuType<MechanicalTableMenu>> mechanical_table = menu("mechanical_table", () -> IForgeMenuType.create(MechanicalTableMenu::new));
}
