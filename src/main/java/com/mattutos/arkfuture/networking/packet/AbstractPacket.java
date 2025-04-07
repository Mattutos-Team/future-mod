package com.mattutos.arkfuture.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public abstract class AbstractPacket {

    public AbstractPacket() {
    }

    public AbstractPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
        // NO-OP
    }

    public abstract void handle(CustomPayloadEvent.Context context);
}
