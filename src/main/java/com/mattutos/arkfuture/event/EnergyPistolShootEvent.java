package com.mattutos.arkfuture.event;

import com.mattutos.arkfuture.ArkFuture;
import com.mattutos.arkfuture.item.EnergyPistolItem;
import com.mattutos.arkfuture.networking.AFPacketHandler;
import com.mattutos.arkfuture.networking.packet.ShootPistolC2SPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArkFuture.MOD_ID)
public class EnergyPistolShootEvent {

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack heldItem = event.getItemStack();
        if (heldItem.getItem() instanceof EnergyPistolItem) {
            AFPacketHandler.sendToServer(new ShootPistolC2SPacket());
        }
    }

}
