package com.mattutos.arkfuture;

import com.mattutos.arkfuture.init.BlockInitTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArkFuture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArkFutureTest {

    @GameTest
    public static void simpleTest(GameTestHelper helper) {
        helper.succeed();
    }

    @SubscribeEvent
    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        event.register(BlockInitTest.class);
    }
}
