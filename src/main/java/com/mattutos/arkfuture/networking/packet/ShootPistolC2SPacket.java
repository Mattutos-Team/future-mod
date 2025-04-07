package com.mattutos.arkfuture.networking.packet;

import com.mattutos.arkfuture.item.EnergyPistolItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ShootPistolC2SPacket extends AbstractPacket {

    public ShootPistolC2SPacket() {
    }

    public ShootPistolC2SPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer serverPlayer = context.getSender();
            ItemStack mainHandItem = serverPlayer.getMainHandItem();

            if (mainHandItem.getItem() instanceof EnergyPistolItem energyPistolItem) {
                energyPistolItem.shoot(serverPlayer, mainHandItem);
            }
        });
        context.setPacketHandled(true);
    }
}
