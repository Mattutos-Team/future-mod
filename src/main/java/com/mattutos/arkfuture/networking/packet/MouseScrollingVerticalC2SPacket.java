package com.mattutos.arkfuture.networking.packet;

import com.mattutos.arkfuture.item.EnergyPistolItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class MouseScrollingVerticalC2SPacket extends AbstractPacket {

    private final double delta;

    public MouseScrollingVerticalC2SPacket(double delta) {
        this.delta = delta;
    }

    public MouseScrollingVerticalC2SPacket(RegistryFriendlyByteBuf buf) {
        this.delta = buf.readDouble();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(delta);
    }

    @Override
    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer serverPlayer = context.getSender();
            ItemStack mainHandItem = serverPlayer.getMainHandItem();

            if (mainHandItem.getItem() instanceof EnergyPistolItem energyPistolItem) {
                energyPistolItem.changePower(serverPlayer, mainHandItem, delta);
            }
        });
    }
}
